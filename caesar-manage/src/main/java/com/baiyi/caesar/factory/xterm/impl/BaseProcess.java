package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.base.SettingName;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.common.util.bae64.CacheKeyUtils;
import com.baiyi.caesar.domain.bo.SSHKeyCredential;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.facade.*;
import com.baiyi.caesar.factory.xterm.ITerminalProcess;
import com.baiyi.caesar.factory.xterm.TerminalProcessFactory;
import com.baiyi.caesar.server.facade.ServerAttributeFacade;
import com.baiyi.caesar.service.server.OcServerService;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import com.baiyi.caesar.service.user.OcUserService;
import com.baiyi.caesar.xterm.config.XTermConfig;
import com.baiyi.caesar.xterm.handler.AuditLogHandler;
import com.baiyi.caesar.xterm.message.BaseMessage;
import com.baiyi.caesar.xterm.model.HostSystem;
import com.baiyi.caesar.xterm.model.JSchSessionMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseProcess implements ITerminalProcess, InitializingBean {

    @Resource
    protected UserFacade userFacade;

    @Resource
    protected UserPermissionFacade userPermissionFacade;

    @Resource
    protected OcUserService ocUserService;

    @Resource
    protected ServerGroupFacade serverGroupFacade;

    @Resource
    protected KeyboxFacade keyboxFacade;

    @Resource
    protected OcServerService ocServerService;

    @Resource
    protected OcUserPermissionService ocUserPermissionService;

    @Resource
    protected TerminalBaseFacade terminalFacade;

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    protected SettingBaseFacade settingFacade;

    @Resource
    private XTermConfig xtermConfig;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    abstract protected BaseMessage getMessage(String message);

    /**
     * 判断用户访问级别 >= ops
     *
     * @param ocUser
     * @return
     */
    protected boolean isOps(OcUser ocUser) {
        return userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel()).isSuccess();
    }

    protected HostSystem buildHostSystem(OcUser ocUser, String host, BaseMessage baseMessage, boolean isAdmin) {
        OcServer ocServer = ocServerService.queryOcServerByIp(host);
        OcUserPermission ocUserPermission = new OcUserPermission();
        ocUserPermission.setUserId(ocUser.getId());
        ocUserPermission.setBusinessId(ocServer.getServerGroupId());
        ocUserPermission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());

        boolean loginType = false;
        if (baseMessage.getLoginUserType() == 1)
            loginType = isAdmin || ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission) != null;

        SSHKeyCredential sshKeyCredential = acqCredential(ocServer,loginType);
        HostSystem hostSystem = new HostSystem();
        hostSystem.setHost(host);
        // 自定义 ssh 端口
        hostSystem.setPort(Integer.parseInt(serverAttributeFacade.getSSHPort(ocServer)));
        hostSystem.setSshKeyCredential(sshKeyCredential);
        hostSystem.setInitialMessage(baseMessage);

        return hostSystem;
    }

    private SSHKeyCredential acqCredential(OcServer ocServer, boolean loginType) {
        String account;
        if (loginType) {
            account = serverAttributeFacade.getAdminAccount(ocServer);
            if (StringUtils.isEmpty(account))
                account = settingFacade.querySetting(SettingName.SERVER_HIGH_AUTHORITY_ACCOUNT);

        } else {
            account = ocServer.getLoginUser();
        }
        return keyboxFacade.getSSHKeyCredential(account);

    }

    protected Boolean isBatch(OcTerminalSession ocTerminalSession) {
        Boolean isBatch = JSchSessionMap.getBatchBySessionId(ocTerminalSession.getSessionId());
        return isBatch == null ? false : isBatch;
    }

    protected void closeSessionInstance(OcTerminalSession ocTerminalSession, String instanceId) {
        try {
            OcTerminalSessionInstance ocTerminalSessionInstance = terminalFacade.queryOcTerminalSessionInstanceByUniqueKey(ocTerminalSession.getSessionId(), instanceId);
            ocTerminalSessionInstance.setCloseTime(new Date());
            ocTerminalSessionInstance.setIsClosed(true);
            ocTerminalSessionInstance.setOutputSize(IOUtil.fileSize(xtermConfig.getAuditLogPath(ocTerminalSession.getSessionId(), instanceId)));
            terminalFacade.updateOcTerminalSessionInstance(ocTerminalSessionInstance);
        } catch (Exception ignored) {
        }
    }

    protected void writeAuditLog(OcTerminalSession ocTerminalSession, String instanceId) {
        AuditLogHandler.writeAuditLog(ocTerminalSession.getSessionId(), instanceId);
    }

    protected void writeCommanderLog(StringBuffer commander, OcTerminalSession ocTerminalSession, String instanceId) {
        AuditLogHandler.writeCommanderLog(commander, ocTerminalSession.getSessionId(), instanceId);
    }

    protected void heartbeat(String sessionId) {
        redisUtil.set(CacheKeyUtils.getTermSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        TerminalProcessFactory.register(this);
    }

}
