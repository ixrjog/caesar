package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.packer.application.ApplicationPacker;
import com.baiyi.caesar.packer.jenkins.JobBuildDecorator;
import com.baiyi.caesar.packer.jenkins.JobDeploymentDecorator;
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
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.NOT_EXTEND;

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
    private JobBuildDecorator jobBuildsDecorator;

    @Resource
    private JobDeploymentDecorator jobDeploymentDecorator;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private ApplicationPacker applicationDecorator;

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.DASHBOARD, key = "'topCard'")
    public DashboardVO.TopCard queryTopCard() {
        DashboardVO.TopCard topCard = new DashboardVO.TopCard();
        topCard.setApplicationTotal(csApplicationService.countAllCsApplication());
        topCard.setBuildJobTotal(csCiJobService.countAllCsCiJob());
        topCard.setDeploymentJobTotal(csCdJobService.countAllCsCdJob());
        topCard.setScmProjectTotal(csGitlabProjectService.countAllCsGitlabProject());
        return topCard;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.DASHBOARD, key = "'latestTasks'")
    public DashboardVO.LatestTasks queryLatestTasks() {

        final int latestTasksLength = 9;

        DashboardVO.LatestTasks vo = DashboardVO.LatestTasks.builder()
                .latestBuildTasks(jobBuildsDecorator.decorator(csCiJobBuildService.queryLatestCsCiJobBuild(latestTasksLength), NOT_EXTEND))
                .buildTaskTotal(csCiJobBuildService.countAllCsCiJobBuild())
                .latestDeploymentTasks(jobDeploymentDecorator.decorator(csCdJobBuildService.queryLatestCsCdJobBuild(latestTasksLength), NOT_EXTEND))
                .deploymentTaskTotal(csCdJobBuildService.countAllCsCdJobBuild())
                .build();

        return vo;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.DASHBOARD, key = "'taskExecutionGroupByHour'")
    public DashboardVO.TaskExecutionGroupByHour queryTaskExecutionGroupByHour() {
        DashboardVO.TaskExecutionGroupByHour charts = new DashboardVO.TaskExecutionGroupByHour();
        charts.setBuildTaskGroupByHours(csCiJobBuildService.queryCiJobBuildGroupByHour());
        charts.setDeploymentTaskGroupByHours(csCdJobBuildService.queryCdJobBuildGroupByHour());
        return charts;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.DASHBOARD, key = "'jobTypeStatistics'")
    public DashboardVO.JobTypeStatistics queryJobTypeStatistics() {
        DashboardVO.JobTypeStatistics charts = new DashboardVO.JobTypeStatistics();
        charts.setBuildJobTypeStatistics(csCiJobService.queryJobTypeTotal());
        return charts;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.DASHBOARD, key = "'taskExecutionGroupByWeek'")
    public DashboardVO.TaskExecutionGroupByWeek queryTaskExecutionGroupByWeek() {
        DashboardVO.TaskExecutionGroupByWeek charts = new DashboardVO.TaskExecutionGroupByWeek();
        charts.setBuildTaskGoupByWeeks(csCiJobService.queryBuildTaskGoupByWeek());
        return charts;
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.DASHBOARD, key = "'hotTopStatistics'")
    public DashboardVO.HotTopStatistics queryHotTopStatistics() {
        final int top = 15;
        DashboardVO.HotTopStatistics charts = new DashboardVO.HotTopStatistics();
        charts.setHotApplications(csCiJobBuildService.queryHotApplication(top)
                .stream().map(e -> applicationDecorator.wrap(e)).collect(Collectors.toList()));
        charts.setHotUsers(csCiJobBuildService.queryHotUser(top));
        return charts;
    }

}
