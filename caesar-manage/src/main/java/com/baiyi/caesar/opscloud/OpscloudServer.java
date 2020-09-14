package com.baiyi.caesar.opscloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
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
 * @Date 2020/9/8 5:47 下午
 * @Version 1.0
 */
@Component
public class OpscloudServer {

    /**
     * 查询服务器分组信息
     *
     * @param serverGroupName
     * @return
     * @throws IOException
     */
    public Map<String, List<OcServer>> queryServerGroupHostPattern(String serverGroupName) throws IOException {
        String url = "/server/group/pattern/query";
        ServerGroupParam.ServerGroupHostPatternQuery query = new ServerGroupParam.ServerGroupHostPatternQuery();
        query.setServerGroupName(serverGroupName);
        JsonNode jsonNode = OpscloudHttpUtils.httpPostExecutor(url, query);
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<Map<String, List<OcServer>>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * 查询服务器组信息
     * @param pageQuery
     * @return
     * @throws IOException
     */
    public DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(com.baiyi.caesar.domain.param.server.ServerGroupParam.PageQuery pageQuery) throws IOException {
        String url = "/server/group/page/query";
        Map<String, String> param = Maps.newHashMap();
        param.put("name", pageQuery.getName());
        param.put("grpType", "");
        param.put("length", pageQuery.getLength().toString());
        param.put("page", pageQuery.getPage().toString());
        JsonNode jsonNode = OpscloudHttpUtils.httpGetExecutor(url, param);
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<ServerGroupVO.ServerGroup>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }

}