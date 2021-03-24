package com.baiyi.caesar.domain.vo.base;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/3/23 6:16 下午
 * @Version 1.0
 */
public class AgoVO {

    public interface IAgo {

        Date getAgoTime();

        void setAgo(String ago);

    }
}
