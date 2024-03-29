package com.baiyi.caesar.packer.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/24 1:53 下午
 * @Version 1.0
 */
@Component
public class JobTplDecorator {

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private TagPacker tagDecorator;

    @Resource
    private JenkinsInstancePacker jenkinsInstanceDecorator;

    public void decorator(JobTplVO.IJobTpl iJobTpl){
        if (IDUtil.isEmpty(iJobTpl.getJobTplId())) return;
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(iJobTpl.getJobTplId());
        if (csJobTpl != null)
            iJobTpl.setJobTpl(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class));
    }

    public JobTplVO.JobTpl decorator(JobTplVO.JobTpl jobTpl, Integer extend) {
        if (extend == 0) return jobTpl;

        jenkinsInstanceDecorator.wrap(jobTpl);
        // 装饰 标签
        tagDecorator.wrap(jobTpl);

        return jobTpl;
    }

}
