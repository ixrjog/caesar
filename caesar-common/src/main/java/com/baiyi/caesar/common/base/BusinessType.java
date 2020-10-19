package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/22 5:46 下午
 * @Version 1.0
 */
public enum BusinessType {
    SERVER(1),
    SERVERGROUP(2),
    USER(3),
    USERGROUP(4),
    CLOUD_DATABASE(5),
    /** 服务器器管理员账户 **/
    SERVER_ADMINISTRATOR_ACCOUNT(6),
    ALIYUN_RAM_ACCOUNT(7),
    /** 应用授权 **/
    APPLICATION(8),
    /** Gitlab项目 **/
    GITLAB_PROJECT(9),
    JENKINS_TPL(10),
    DINGTALK(11),
    ALIYUN_OSS_BUCKET(12),
    /** 构建任务 **/
    APPLICATION_BUILD_JOB(13),
    /** 部署任务 **/
    APPLICATION_DEPLOYMENT_JOB(14),
    ;

    private int type;

    BusinessType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
