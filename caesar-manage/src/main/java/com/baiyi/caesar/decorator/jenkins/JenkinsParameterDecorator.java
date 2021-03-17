package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.JenkinsUtil;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsVO;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/3/17 3:59 下午
 * @Version 1.0
 */
@Component
public class JenkinsParameterDecorator {

    public void decorator(JenkinsVO.IJenkinsParameter iJenkinsParameter){
        // 参数
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtil.convert(iJenkinsParameter.getParameterYaml());
        Map<String, String> params = JenkinsUtil.convert(jenkinsJobParameters);
        iJenkinsParameter.setParameters(params);
    }
}
