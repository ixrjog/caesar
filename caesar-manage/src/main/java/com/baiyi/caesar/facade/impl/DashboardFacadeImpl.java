package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.ApplicationDecorator;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dashboard.DashboardVO;
import com.baiyi.caesar.facade.DashboardFacade;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/5 3:15 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class DashboardFacadeImpl implements DashboardFacade {

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private JobBuildDecorator jobBuildDecorator;

    @Resource
    private JobDeploymentDecorator jobDeploymentDecorator;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private ApplicationDecorator applicationDecorator;

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'topCard'")
    public DashboardVO.TopCard queryTopCard() {
        DashboardVO.TopCard topCard = new DashboardVO.TopCard();
        topCard.setApplicationTotal(csApplicationService.countAllCsApplication());
        topCard.setBuildJobTotal(csCiJobService.countAllCsCiJob());
        topCard.setDeploymentJobTotal(csCdJobService.countAllCsCdJob());
        topCard.setScmProjectTotal(csGitlabProjectService.countAllCsGitlabProject());
        return topCard;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'latestTasks'")
    public DashboardVO.LatestTasks queryLatestTasks() {
        DashboardVO.LatestTasks latestTasks = new DashboardVO.LatestTasks();
        final int LatestTasksLength = 9;

        List<CiJobBuildVO.JobBuild> latestBuildTasks = jobBuildDecorator.decorator(csCiJobBuildService.queryLatestCsCiJobBuild(LatestTasksLength),0);

                latestTasks.setLatestBuildTasks(latestBuildTasks);
        latestTasks.setBuildTaskTotal(csCiJobBuildService.countAllCsCiJobBuild());

        List<CdJobBuildVO.JobBuild> latestDeploymentTasks = csCdJobBuildService.queryLatestCsCdJobBuild(LatestTasksLength)
                .stream().map(e -> jobDeploymentDecorator.decorator(BeanCopierUtils.copyProperties(e, CdJobBuildVO.JobBuild.class), 0)).collect(Collectors.toList());
        latestTasks.setLatestDeploymentTasks(latestDeploymentTasks);
        latestTasks.setDeploymentTaskTotal(csCdJobBuildService.countAllCsCdJobBuild());
        return latestTasks;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'taskExecutionGroupByHour'")
    public DashboardVO.TaskExecutionGroupByHour queryTaskExecutionGroupByHour() {
        DashboardVO.TaskExecutionGroupByHour charts = new DashboardVO.TaskExecutionGroupByHour();
        charts.setBuildTaskGroupByHours(csCiJobBuildService.queryCiJobBuildGroupByHour());
        charts.setDeploymentTaskGroupByHours(csCdJobBuildService.queryCdJobBuildGroupByHour());
        return charts;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'jobTypeStatistics'")
    public DashboardVO.JobTypeStatistics queryJobTypeStatistics() {
        DashboardVO.JobTypeStatistics charts = new DashboardVO.JobTypeStatistics();
        charts.setBuildJobTypeStatistics(csCiJobService.queryJobTypeTotal());
        return charts;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'taskExecutionGroupByWeek'")
    public DashboardVO.TaskExecutionGroupByWeek queryTaskExecutionGroupByWeek() {
        DashboardVO.TaskExecutionGroupByWeek charts = new DashboardVO.TaskExecutionGroupByWeek();
        charts.setBuildTaskGoupByWeeks(csCiJobService.queryBuildTaskGoupByWeek());
        return charts;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'hotTopStatistics'")
    public DashboardVO.HotTopStatistics queryHotTopStatistics() {
        final int top = 15;
        DashboardVO.HotTopStatistics charts = new DashboardVO.HotTopStatistics();
        charts.setHotApplications(csCiJobBuildService.queryHotApplication(top)
                .stream().map(e->applicationDecorator.decorator(e)).collect(Collectors.toList()));
        charts.setHotUsers(csCiJobBuildService.queryHotUser(top));
        return charts;
    }

}
