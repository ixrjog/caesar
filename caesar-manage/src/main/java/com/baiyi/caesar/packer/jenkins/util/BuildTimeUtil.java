package com.baiyi.caesar.packer.jenkins.util;

import com.baiyi.caesar.common.util.TimeUtil;
import com.baiyi.caesar.domain.vo.base.BuildTimeVO;

/**
 * @Author baiyi
 * @Date 2021/3/23 6:25 下午
 * @Version 1.0
 */
public class BuildTimeUtil {

    public static void decorator(BuildTimeVO.IBuildTime iBuildTime) {
        if (iBuildTime.getStartTime() != null || iBuildTime.getEndTime() != null) return;
        long buildTime = iBuildTime.getEndTime().getTime() - iBuildTime.getStartTime().getTime();
        iBuildTime.setBuildTime(TimeUtil.acqBuildTime(buildTime));
    }

}
