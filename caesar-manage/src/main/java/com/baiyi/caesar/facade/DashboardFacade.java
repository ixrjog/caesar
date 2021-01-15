package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.vo.dashboard.DashboardVO;

/**
 * @Author baiyi
 * @Date 2020/11/5 3:15 下午
 * @Version 1.0
 */
public interface DashboardFacade {

    DashboardVO.TopCard queryTopCard();

    DashboardVO.LatestTasks queryLatestTasks();

    DashboardVO.TaskExecutionGroupByHour queryTaskExecutionGroupByHour();

    DashboardVO.JobTypeStatistics queryJobTypeStatistics();

    DashboardVO.TaskExecutionGroupByWeek queryTaskExecutionGroupByWeek();

    DashboardVO.HotTopStatistics queryHotTopStatistics();
}
