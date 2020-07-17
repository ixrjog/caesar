package com.baiyi.caesar.server.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerAttribute;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.vo.server.ServerAttributeVO;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:28 上午
 * @Version 1.0
 */
public interface ServerAttributeFacade {

    List<ServerAttributeVO.ServerAttribute> queryServerGroupAttribute(OcServerGroup ocServerGroup);

    List<ServerAttributeVO.ServerAttribute> queryServerAttribute(OcServer ocServer);

    List<OcServerAttribute> queryServerAttributeById(int serverId);

   void deleteServerAttributeByList(List<OcServerAttribute> serverAttributeList);

    BusinessWrapper<Boolean> saveServerAttribute(ServerAttributeVO.ServerAttribute serverAttribute);

    Map<String, String> getServerGroupAttributeMap(OcServerGroup ocServerGroup);

    Map<String, String> getServerAttributeMap(OcServer ocServer);

    String getManageIp(OcServer ocServer);

    String getSSHPort(OcServer ocServer);

    // Map<String, String> getServerAttributeMap(int serverId);
}
