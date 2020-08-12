package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/29 2:08 下午
 * @Version 1.0
 */
@Component
public class CiJobDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private CsDingtalkService csDingtalkService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    private TagDecorator tagDecorator;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    public CiJobVO.CiJob decorator(CiJobVO.CiJob ciJob, Integer extend) {
        if (extend == 0) return ciJob;
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ciJob.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            ciJob.setEnv(env);
        }
        // 钉钉
        if (!IDUtils.isEmpty(ciJob.getDingtalkId())) {
            CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(ciJob.getDingtalkId());
            if (csDingtalk != null)
                ciJob.setDingtalk(BeanCopierUtils.copyProperties(csDingtalk, DingtalkVO.Dingtalk.class));
        }
        // 任务模版
        if (!IDUtils.isEmpty(ciJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(ciJob.getJobTplId());
            if (csJobTpl != null)
                ciJob.setJobTpl(BeanCopierUtils.copyProperties(csJobTpl, JobTplVO.JobTpl.class));
        }
        // SCM
        if (!IDUtils.isEmpty(ciJob.getScmMemberId())) {
            CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(ciJob.getScmMemberId());
            if (csApplicationScmMember != null) {
                ciJob.setTags(tagDecorator.decorator(BusinessType.GITLAB_PROJECT.getType(), csApplicationScmMember.getScmId()));
                ciJob.setScmMember(BeanCopierUtils.copyProperties(csApplicationScmMember, ApplicationVO.ScmMember.class));
            }
        }
        // Bucket
        if (!IDUtils.isEmpty(ciJob.getOssBucketId())) {
            CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(ciJob.getOssBucketId());
            if (csOssBucket != null) {
                ciJob.setBucket(BeanCopierUtils.copyProperties(csOssBucket, OssBucketVO.Bucket.class));
            }
        }

        JobBuildParam.JobBuildPageQuery query = new JobBuildParam.JobBuildPageQuery();
        query.setCiJobId(ciJob.getId());
        query.setExtend(1);
        ciJob.setBuildViews(acqCiJobBuildView(query));

        return ciJob;
    }

    private List<CiJobBuildVO.JobBuildView> acqCiJobBuildView(JobBuildParam.JobBuildPageQuery pageQuery) {
        pageQuery.setPage(1);
        pageQuery.setLength(3);
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(pageQuery);
        List<CsCiJobBuild> builds = table.getData();
        return builds.stream().map(e -> {
            CiJobBuildVO.JobBuildView jobBuildView = new CiJobBuildVO.JobBuildView();
            jobBuildView.setBuildNumber(e.getJobBuildNumber());
            jobBuildView.setBuilding(!e.getFinalized());
            if (!e.getFinalized()) {
                jobBuildView.setColor("#E07D06");
            } else {
                if (e.getBuildStatus().equals("SUCCESS")) {
                    jobBuildView.setColor("#17BA14");
                } else {
                    jobBuildView.setColor("#DD3E03");
                }
            }
            return jobBuildView;
        }).collect(Collectors.toList());
    }
}
