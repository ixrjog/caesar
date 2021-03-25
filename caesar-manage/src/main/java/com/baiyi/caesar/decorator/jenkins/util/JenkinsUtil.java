package com.baiyi.caesar.decorator.jenkins.util;

import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.bae64.FileSizeUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.vo.build.BuildArtifactVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsVO;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/25 1:25 下午
 * @Version 1.0
 */
public class JenkinsUtil {

    public static void decoratorParameter(JenkinsVO.IJenkinsParameter iJenkinsParameter) {
        // 参数
        JenkinsJobParameters jenkinsJobParameters = com.baiyi.caesar.common.util.JenkinsUtil.convert(iJenkinsParameter.getParameterYaml());
        Map<String, String> params = com.baiyi.caesar.common.util.JenkinsUtil.convert(jenkinsJobParameters);
        iJenkinsParameter.setParameters(params);
    }


    public static List<BuildArtifactVO.BuildArtifact> decoratorBuildArtifacts(List<CsJobBuildArtifact> artifacts, CsOssBucket csOssBucket) {
        if (CollectionUtils.isEmpty(artifacts))
            return Lists.newArrayList();

        return artifacts.stream().map(e ->
                convert(e, csOssBucket)
        ).collect(Collectors.toList());
    }

    private static BuildArtifactVO.BuildArtifact convert(CsJobBuildArtifact csJobBuildArtifact, CsOssBucket csOssBucket) {
        BuildArtifactVO.BuildArtifact buildArtifact = BeanCopierUtil.copyProperties(csJobBuildArtifact, BuildArtifactVO.BuildArtifact.class);
        if (buildArtifact.getArtifactSize() != null && buildArtifact.getArtifactSize() >= 0)
            buildArtifact.setArtifactFileSize(FileSizeUtils.formetFileSize(buildArtifact.getArtifactSize()));
        if (csOssBucket != null)
            buildArtifact.setOssUrl(Joiner.on("/").join(buildOssBucketUrl(csOssBucket), buildArtifact.getStoragePath()));
        return buildArtifact;
    }

    private static String buildOssBucketUrl(CsOssBucket csOssBucket) {
        return "https://" + Joiner.on(".").join(csOssBucket.getName(), csOssBucket.getExtranetEndpoint());

    }

}
