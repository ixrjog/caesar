package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
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
    private CsJenkinsInstanceService jenkinsInstanceService;

    @Resource
    private TagDecorator tagDecorator;

    public JobTplVO.JobTpl decorator(JobTplVO.JobTpl jobTpl, Integer extend) {
        if (extend == 0) return jobTpl;

        CsJenkinsInstance csJenkinsInstance = jenkinsInstanceService.queryCsJenkinsInstanceById(jobTpl.getJenkinsInstanceId());
        if (csJenkinsInstance != null) {
            jobTpl.setJenkinsInstance(BeanCopierUtil.copyProperties(csJenkinsInstance, JenkinsInstanceVO.Instance.class));
        }
        // 装饰 标签
        jobTpl.setTags(tagDecorator.decorator(BusinessType.JENKINS_TPL.getType(), jobTpl.getId()));
        return jobTpl;
    }

}
