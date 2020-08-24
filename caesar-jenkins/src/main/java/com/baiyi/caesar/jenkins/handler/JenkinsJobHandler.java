package com.baiyi.caesar.jenkins.handler;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/8/6 2:02 下午
 * @Version 1.0
 */
@Component
public class JenkinsJobHandler {

    @Retryable(value = RetryException.class, maxAttempts = 100, backoff = @Backoff(delay = 3000))
    public Build getJobBuildByNumber(JobWithDetails job, int number) throws RetryException {
        while (true) {
            // 判断队列
            try {
                job = job.details();
                if (!job.isInQueue()) {
                    Build build = job.getBuildByNumber(number);
                    if (build != null)
                        return build;
                    throw new RetryException("");
                }
            } catch (IOException e) {
                throw new RetryException("");
            }
        }
    }
}
