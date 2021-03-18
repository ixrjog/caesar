package com.baiyi.caesar.decorator.application.base;

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

    protected String acqBuildViewColor(Boolean finalized, String buildStatus) {
        if (!finalized) {
            return colors.RUNNING;
        } else {
            if ("SUCCESS".equals(buildStatus)) {
                return colors.SUCCESS;
            } else {
                return colors.FAILURE;
            }
        }
    }
}
