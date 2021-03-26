package com.baiyi.caesar.domain.vo.base;

/**
 * @Author baiyi
 * @Date 2021/3/25 2:44 下午
 * @Version 1.0
 */
public class JobBuildVO {

    public interface IJobBuild {

        String getJobName();

        Integer getEngineBuildNumber();

        void setJobBuildUrl(String jobBuildUrl);

    }
}
