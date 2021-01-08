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

    public static final String SUCCESS_COLOR="#17BA14";
    public static final String FAILURE_COLOR="#DD3E03";
    public static final String RUNNING_COLOR="#E07D06";

    protected void assembleJobBuildView(CiJobBuildVO.JobBuildView jobBuildView,Boolean finalized ,String buildStatus){
        if (!finalized) {
            jobBuildView.setColor(RUNNING_COLOR);
        } else {
            if ("SUCCESS".equals(buildStatus) ) {
                jobBuildView.setColor(SUCCESS_COLOR);
            } else {
                jobBuildView.setColor(FAILURE_COLOR);
            }
        }
    }
}
