package com.baiyi.caesar.opscloud4;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.JSONUtil;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.opscloud4.model.ServerGroupModel;
import com.baiyi.caesar.opscloud4.model.ServerModel;
import com.baiyi.caesar.opscloud4.param.ServerGroupParam;
import com.baiyi.caesar.opscloud4.service.Opscloud4ServerGroupService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/25 5:55 下午
 * @Version 1.0
 */
public class Opscloud4ServerGroupServiceTest extends BaseUnit {

    @Resource
    private Opscloud4ServerGroupService opscloud4ServerGroupService;

    @Test
    void queryServerGroupHostPatternTest() {
        ServerGroupParam.ServerGroupEnvHostPatternQuery query = ServerGroupParam.ServerGroupEnvHostPatternQuery.builder()
                .serverGroupName("group_account")
                .envType(4)
                .build();
        Map<String, List<ServerModel.Server>> map = opscloud4ServerGroupService.queryServerGroupHostPattern(query);
        System.out.println(JSONUtil.writeValueAsString(map));
    }

    // crawler-tools-urc
    @Test
    void queryServerGroupPageTest() {
        ApplicationParam.ServerGroupPageQuery query = ApplicationParam.ServerGroupPageQuery.builder()
                .build();
        query.setName( "group_account");
        query.setPage(1);
        query.setLength(10);
        ServerGroupModel.DataTable dataTable = opscloud4ServerGroupService.queryServerGroupPage(query);
        System.out.println(JSONUtil.writeValueAsString(dataTable));
    }

}
