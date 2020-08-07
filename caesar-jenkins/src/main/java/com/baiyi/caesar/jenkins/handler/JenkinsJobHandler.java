package com.baiyi.caesar.jenkins.handler;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/6 2:02 下午
 * @Version 1.0
 */
@Component
public class JenkinsJobHandler {

    @Retryable(value = RuntimeException.class, maxAttempts = 100, backoff = @Backoff(delay = 3000))
    public Build getJobBuildByNumber(JobWithDetails job, int number) throws RuntimeException {
        while (true){
            // 判断队列
            if(!job.isInQueue()) {
                Build build = job.getLastBuild();
                if (build.getNumber() == number) return build;
                try {
                    List<Build> builds = job.getAllBuilds();
                    for (Build b : builds)
                        if (build.getNumber() == number) return b;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException();
            }
        }
    }
}
