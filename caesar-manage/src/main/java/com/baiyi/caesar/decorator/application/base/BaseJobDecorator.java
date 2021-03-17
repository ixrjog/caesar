package com.baiyi.caesar.decorator.application.base;

import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/1/8 10:48 上午
 * @Version 1.0
 */
@Component
public class BaseJobDecorator {

    public interface colors {
        String SUCCESS = "#17BA14";
        String FAILURE = "#DD3E03";
        String RUNNING = "#E07D06";
    }

    protected void assembleJobBuildView(CiJobBuildVO.JobBuildView jobBuildView, Boolean finalized, String buildStatus) {
        if (!finalized) {
            jobBuildView.setColor(colors.RUNNING);
        } else {
            if ("SUCCESS".equals(buildStatus)) {
                jobBuildView.setColor(colors.SUCCESS);
            } else {
                jobBuildView.setColor(colors.FAILURE);
            }
        }
    }
}
