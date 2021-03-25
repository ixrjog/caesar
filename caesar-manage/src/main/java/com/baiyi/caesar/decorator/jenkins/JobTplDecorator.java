package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
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
    private TagDecorator tagDecorator;

    @Resource
    private JenkinsInstanceDecorator jenkinsInstanceDecorator;

    public void decorator(JobTplVO.IJobTpl iJobTpl){
        if (IDUtil.isEmpty(iJobTpl.getJobTplId())) return;
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(iJobTpl.getJobTplId());
        if (csJobTpl != null)
            iJobTpl.setJobTpl(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class));
    }

    public JobTplVO.JobTpl decorator(JobTplVO.JobTpl jobTpl, Integer extend) {
        if (extend == 0) return jobTpl;

        jenkinsInstanceDecorator.decorator(jobTpl);
        // 装饰 标签
        tagDecorator.decorator(jobTpl);

        return jobTpl;
    }

}
