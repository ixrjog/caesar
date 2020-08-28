package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.RedisKeyUtils;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.ICiJobHandler;
import com.baiyi.caesar.factory.jenkins.CiJobHandlerFactory;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:51 下午
 * @Version 1.0
 */
@Component
public class JobFacade {

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private JobBuildDecorator jobBuildDecorator;

    @Resource
    private RedisUtil redisUtil;

    public BusinessWrapper<Boolean> buildCiJob(JobBuildParam.BuildParam buildParam) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById((buildParam.getCiJobId()));
        ICiJobHandler jenkinsJobHandler = CiJobHandlerFactory.getCiJobByKey(csCiJob.getJobType());
        if (StringUtils.isEmpty(buildParam.getBranch()))
            buildParam.setBranch(csCiJob.getBranch());
        jenkinsJobHandler.build(csCiJob, buildParam);
        return BusinessWrapper.SUCCESS;
    }

    public DataTable<CiJobBuildVO.JobBuild> queryCiJobBuildPage(JobBuildParam.JobBuildPageQuery pageQuery) {
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(pageQuery);
        List<CiJobBuildVO.JobBuild> page = BeanCopierUtils.copyListProperties(table.getData(), CiJobBuildVO.JobBuild.class);
        return new DataTable<>(page.stream().map(e -> jobBuildDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    public List<CiJobBuildVO.JobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query){
        if(query.getSize() == null)
            query.setSize(10);
       return csCiJobBuildService.queryCiJobBuildArtifact(query)
               .stream().map(e -> jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(e,CiJobBuildVO.JobBuild.class), 1)).collect(Collectors.toList());
    }

    public CiJobBuildVO.JobBuild queryCiJobBuildByBuildId(@Valid int buildId) {
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(buildId);
        return jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class), 1);
    }

    public void trackJobBuildTask() {
        List<CsCiJobBuild> csCiJobBuilds = csCiJobBuildService.queryCsCiJobBuildByFinalized(false);
        if (CollectionUtils.isEmpty(csCiJobBuilds)) return;
        csCiJobBuilds.forEach(e -> {
            String key = RedisKeyUtils.getJobBuildKey(e.getId());
            if (!redisUtil.hasKey(key)) {
                CsCiJob csCiJob = csCiJobService.queryCsCiJobById((e.getCiJobId()));
                ICiJobHandler jenkinsJobHandler = CiJobHandlerFactory.getCiJobByKey(csCiJob.getJobType());
                jenkinsJobHandler.trackJobBuild(e);
            }
        });
    }

}
