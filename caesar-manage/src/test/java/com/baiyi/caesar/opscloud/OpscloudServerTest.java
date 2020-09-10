package com.baiyi.caesar.opscloud;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/9/9 2:09 下午
 * @Version 1.0
 */
public class OpscloudServerTest extends BaseUnit {

    @Resource
    private OpscloudServer opscloudServer;

    @Test
    void testQueryServer() {
        try {
            Map<String, List<OcServer>> map = opscloudServer.queryServerGroupHostPattern("group_opscloud");
            System.err.println(map);
        } catch (IOException e) {
        }
    }

    @Test
    void testQueryServerGroup() {
        try {
            ServerGroupParam.PageQuery pageQuery = new ServerGroupParam.PageQuery();
            pageQuery.setName("caesar");
            pageQuery.setPage(1);
            pageQuery.setLength(10);
            DataTable<ServerGroupVO.ServerGroup> dataTable = opscloudServer.queryServerGroupPage(pageQuery);
            System.err.println(dataTable);
        } catch (IOException e) {

        }
    }
}
