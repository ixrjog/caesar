package com.baiyi.caesar.domain.vo.base;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/3/23 6:26 下午
 * @Version 1.0
 */
public class BuildTimeVO {

    public interface IBuildTime {

        Date getStartTime();
        Date getEndTime();
        void setBuildTime(String buildTime);

    }
}
