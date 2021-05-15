package com.baiyi.caesar.packer.base;

import com.baiyi.caesar.common.util.TimeAgoUtil;
import com.baiyi.caesar.packer.aliyun.OssBucketPacker;
import com.baiyi.caesar.packer.application.BuildViewPacker;
import com.baiyi.caesar.packer.dingtalk.DingtalkDecorator;
import com.baiyi.caesar.packer.env.EnvDecorator;
import com.baiyi.caesar.packer.gitlab.GitlabInstanceDecorator;
import com.baiyi.caesar.packer.gitlab.GitlabVersionDecorator;
import com.baiyi.caesar.packer.jenkins.JenkinsVersionDecorator;
import com.baiyi.caesar.packer.jenkins.JobTplDecorator;
import com.baiyi.caesar.packer.jenkins.util.BuildTimeUtil;
import com.baiyi.caesar.packer.jenkins.util.JenkinsUtil;
import com.baiyi.caesar.packer.server.ServerDecorator;
import com.baiyi.caesar.packer.server.ServerGroupDecorator;
import com.baiyi.caesar.packer.sonar.SonarQubeDecorator;
import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.packer.user.*;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.domain.vo.base.AgoVO;
import com.baiyi.caesar.domain.vo.base.BuildTimeVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.sonar.SonarQubeVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/3/24 10:11 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class BaseDecorator<T> {

    public static final int NOT_EXTEND = 0;

    @Resource
    private UserDecorator userDecorator;

    @Resource
    private EnvDecorator envDecorator;

    @Resource
    private JobTplDecorator jobTplDecorator;

    @Resource
    private BuildViewPacker buildViewDecorator;

    @Resource
    private DingtalkDecorator dingtalkDecorator;

    @Resource
    private OssBucketPacker ossBucketDecorator;

    @Resource
    private SonarQubeDecorator sonarQubeDecorator;

    @Resource
    private UserGroupDecorator userGroupDecorator;

    @Resource
    private UserPermissionServerGroupDecorator userPermissionServerGroupDecorator;

    @Resource
    private UserApiTokenDecorator userApiTokenDecorator;

    @Resource
    private UserCredentialDecorator userCredentialDecorator;

    @Resource
    private TagPacker tagDecorator;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    @Resource
    private GitlabInstanceDecorator gitlabInstanceDecorator;

    @Resource
    private GitlabVersionDecorator gitlabVersionDecorator;

    @Resource
    private ServerDecorator serverDecorator;

    @Resource
    private JenkinsVersionDecorator jenkinsVersionDecorator;

    protected void decoratorJenkinsInstance(T t) {
        if (t instanceof JenkinsInstanceVO.IJenkinsVersion)
            jenkinsVersionDecorator.wrap((JenkinsInstanceVO.IJenkinsVersion) t);
        if (t instanceof ServerVO.IServer)
            serverDecorator.decorator((ServerVO.IServer) t);
        if (t instanceof ServerGroupVO.IServerGroup)
            serverGroupDecorator.decorator((ServerGroupVO.IServerGroup) t);

    }


    protected void decoratorGitlabInstance(T t) {
        if (t instanceof ServerVO.IServer)
            serverDecorator.decorator((ServerVO.IServer) t);
        if (t instanceof GitlabInstanceVO.IVersion)
            gitlabVersionDecorator.decorator((GitlabInstanceVO.IVersion) t); // 版本
    }


    protected void decoratorBaseGitlab(T t) {
        if (t instanceof GitlabInstanceVO.IInstance)
            gitlabInstanceDecorator.decorator((GitlabInstanceVO.IInstance) t);

        // 装饰 标签
        if (t instanceof TagVO.ITags)
            tagDecorator.wrap((TagVO.ITags) t);
    }

    protected void decoratorServer(T t) {
        // 环境
        if (t instanceof EnvVO.IEnv)
            envDecorator.decorator((EnvVO.IEnv) t);
        // 标签
        if (t instanceof TagVO.ITags)
            tagDecorator.wrap((TagVO.ITags) t);

        // 服务器组
        if (t instanceof ServerGroupVO.IServerGroup)
            serverGroupDecorator.decorator((ServerGroupVO.IServerGroup) t);
    }

    protected void decoratorUser(T t) {
        if (t instanceof UserGroupVO.IUserGroups)
            userGroupDecorator.decorator((UserGroupVO.IUserGroups) t);  // 装饰 用户组
        if (t instanceof UserVO.User) {
            userPermissionServerGroupDecorator.decorator((UserVO.User) t); // 装饰 服务器组
            userApiTokenDecorator.decorator((UserVO.User) t);  // 装饰 ApiToken
            userCredentialDecorator.decorator((UserVO.User) t);    // 装饰 凭据
        }
    }

    protected void decoratorJob(T t) {
        // 装饰 环境信息
        if (t instanceof EnvVO.IEnv)
            envDecorator.decorator((EnvVO.IEnv) t);

        if (t instanceof JobTplVO.IJobTpl)
            jobTplDecorator.decorator((JobTplVO.IJobTpl) t);  // 任务模版

        if (t instanceof JenkinsVO.IJenkinsParameter)
            JenkinsUtil.decoratorParameter((JenkinsVO.IJenkinsParameter) t);  // 参数

        if (t instanceof CiJobBuildVO.IBuildView)
            buildViewDecorator.wrap((CiJobBuildVO.IBuildView) t);    // buildViews

        if (t instanceof CdJobBuildVO.IBuildView)
            buildViewDecorator.wrap((CdJobBuildVO.IBuildView) t);    // buildViews

        if (t instanceof DingtalkVO.IDingtalk)
            dingtalkDecorator.decorator((DingtalkVO.IDingtalk) t);   // 钉钉

        if (t instanceof OssBucketVO.IBucket)
            ossBucketDecorator.wrap((OssBucketVO.IBucket) t); // Bucket

        if (t instanceof SonarQubeVO.ISonarQube)
            sonarQubeDecorator.decorator((SonarQubeVO.ISonarQube) t);    // SonarQube
    }

    protected void decoratorJobBuild(T t, Integer extend) {
        if (t instanceof UserVO.IUser)
            userDecorator.decorator((UserVO.IUser) t);

        if (t instanceof AgoVO.IAgo)
            TimeAgoUtil.decorator((AgoVO.IAgo) t);

        if (NOT_EXTEND == extend) return;
        if (t instanceof BuildTimeVO.IBuildTime)
            BuildTimeUtil.decorator((BuildTimeVO.IBuildTime) t);
    }


    public interface colors {
        String SUCCESS = "#17BA14";
        String FAILURE = "#DD3E03";
        String RUNNING = "#E07D06";
    }

    public static String acqBuildViewColor(Boolean finalized, String buildStatus) {
        if (!finalized) {
            return colors.RUNNING;
        } else {
            if ("SUCCESS" .equals(buildStatus)) {
                return colors.SUCCESS;
            } else {
                return colors.FAILURE;
            }
        }
    }

}
