package com.baiyi.caesar.decorator.base;

import com.baiyi.caesar.common.util.TimeAgoUtil;
import com.baiyi.caesar.decorator.aliyun.OssBucketDecorator;
import com.baiyi.caesar.decorator.application.BuildViewDecorator;
import com.baiyi.caesar.decorator.dingtalk.DingtalkDecorator;
import com.baiyi.caesar.decorator.env.EnvDecorator;
import com.baiyi.caesar.decorator.jenkins.JenkinsParameterDecorator;
import com.baiyi.caesar.decorator.jenkins.JobTplDecorator;
import com.baiyi.caesar.decorator.jenkins.util.BuildTimeUtil;
import com.baiyi.caesar.decorator.server.ServerGroupDecorator;
import com.baiyi.caesar.decorator.sonar.SonarQubeDecorator;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.decorator.user.*;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.domain.vo.base.AgoVO;
import com.baiyi.caesar.domain.vo.base.BuildTimeVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
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
    private JenkinsParameterDecorator jenkinsParameterDecorator;

    @Resource
    private BuildViewDecorator buildViewDecorator;

    @Resource
    private DingtalkDecorator dingtalkDecorator;

    @Resource
    private OssBucketDecorator ossBucketDecorator;

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
    private TagDecorator tagDecorator;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    protected void decoratorServer(T t) {
        // 环境
        if (t instanceof EnvVO.IEnv)
            envDecorator.decorator((EnvVO.IEnv) t);
        // 标签
        if (t instanceof TagVO.ITags)
            tagDecorator.decorator((TagVO.ITags) t);

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
            jenkinsParameterDecorator.decorator((JenkinsVO.IJenkinsParameter) t);   // 参数

        if (t instanceof CiJobBuildVO.IBuildView)
            buildViewDecorator.decorator((CiJobBuildVO.IBuildView) t);    // buildViews

        if (t instanceof CdJobBuildVO.IBuildView)
            buildViewDecorator.decorator((CdJobBuildVO.IBuildView) t);    // buildViews

        if (t instanceof DingtalkVO.IDingtalk)
            dingtalkDecorator.decorator((DingtalkVO.IDingtalk) t);   // 钉钉

        if (t instanceof OssBucketVO.IBucket)
            ossBucketDecorator.decorator((OssBucketVO.IBucket) t); // Bucket

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
