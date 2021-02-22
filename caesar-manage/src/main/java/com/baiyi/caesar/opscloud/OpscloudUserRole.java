package com.baiyi.caesar.opscloud;

import com.baiyi.caesar.domain.vo.auth.UserRoleVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/10/8 10:41 上午
 * @Version 1.0
 */
@Component
public class OpscloudUserRole {

    private interface API {
        String QUERY_USER_ROLES_API = "/auth/user/role/query";
    }

    /**
     * 查询用户角色详情
     *
     * @param username
     * @return
     * @throws IOException
     */
    public List<UserRoleVO.UserRole> queryUserRoles(String username) throws IOException {
        Map<String, String> param = Maps.newHashMap();
        param.put("username", username);
        JsonNode jsonNode = OpscloudHttpUtils.httpGetExecutor(API.QUERY_USER_ROLES_API, param);
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<List<UserRoleVO.UserRole>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return Collections.emptyList();
        }
    }
}
