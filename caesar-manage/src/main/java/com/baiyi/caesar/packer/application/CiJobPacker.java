package com.baiyi.caesar.packer.application;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.packer.base.BaseDecorator;
import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/29 2:08 下午
 * @Version 1.0
 */
@Component
public class CiJobPacker extends BaseDecorator {

    @Resource
    private TagPacker tagPacker;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEnginePacker jobEngineDecorator;

    @Resource
    private CdJobPacker cdJobPacker;

    public CiJobVO.CiJob wrap(CiJobVO.CiJob ciJob, CsJobTpl csJobTpl) {
        List<CsJobEngine> csCiJobEngines = csJobEngineService.queryCsJobEngineByJobId(BuildType.BUILD.getType(), ciJob.getId());
        AtomicReference<Boolean> needUpgrade = new AtomicReference<>(false);
        if (!CollectionUtils.isEmpty(csCiJobEngines)) {
            ciJob.setJobEngines(
                    csCiJobEngines.stream().map(e -> {
                        JobEngineVO.JobEngine jobEngine = BeanCopierUtil.copyProperties(e, JobEngineVO.JobEngine.class);
                        jobEngine.setNeedUpgrade(csJobTpl.getTplVersion() > jobEngine.getTplVersion());
                        if (jobEngine.getNeedUpgrade())
                            needUpgrade.set(true);
                        return jobEngineDecorator.wrap(jobEngine);
                    }).collect(Collectors.toList())
            );
        }
        ciJob.setNeedUpgrade(needUpgrade.get());
        return ciJob;
    }

    public CiJobVO.CiJob wrap(CiJobVO.CiJob ciJob, Integer extend) {
        if (extend == 0) return ciJob;
        decoratorJob(ciJob);
        cdJobPacker.decorator(ciJob); // Deployment Job

        // SCM
        if (!IDUtil.isEmpty(ciJob.getScmMemberId())) {
            CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(ciJob.getScmMemberId());
            if (csApplicationScmMember != null) {
                ciJob.setScmMember(BeanCopierUtil.copyProperties(csApplicationScmMember, ApplicationVO.ScmMember.class));
                ciJob.setBusinessType(BusinessType.GITLAB_PROJECT.getType());
                tagPacker.wrap(ciJob);
            }
        }
        return ciJob;
    }


}
