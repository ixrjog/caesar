package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.GitlabTokenUtil;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHooksVO;
import com.baiyi.caesar.facade.GitlabFacade;
import org.gitlab.api.models.GitlabUser;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/12/22 3:19 下午
 * @Version 1.0
 */
public class GitlabFacadeImplTest extends BaseUnit {

    @Resource
    private GitlabFacade gitlabFacade;


    /**
     * {
     * "event_name": "project_create",
     * "created_at": "2020-12-22T06:56:30Z",
     * "updated_at": "2020-12-22T06:56:30Z",
     * "name": "baiyitest-1",
     * "path": "baiyitest-1",
     * "path_with_namespace": "baiyi/baiyitest-1",
     * "project_id": 211,
     * "owner_name": "baiyi",
     * "owner_email": "baiyi@xinc818.group",
     * "project_visibility": "private"
     * }
     */

    @Test
    void systemHooksV1Test() {
        GitlabTokenUtil.setToken("8d8e35c2952d716f29c831b728fa7556");
        GitlabHooksVO.SystemHook systemHook = new GitlabHooksVO.SystemHook();
        systemHook.setEventName("project_create");
        systemHook.setProjectId(211);
        systemHook.setName("baiyitest-1");
        gitlabFacade.systemHooksV1(systemHook);
    }

    @Test
    void gitlabTokenTest() {
        GitlabTokenUtil.setToken("8d8e35c2952d716f29c831b728fa7556");
        String token = GitlabTokenUtil.getToken();
        System.out.println(token);
    }

    @Test
    void gitlabTest() {
        GitlabUser gitlabUser = gitlabFacade.queryUser("gitlab-1", "baiyi");
        boolean result = gitlabFacade.isGroupMember("gitlab-1", 109, gitlabUser);
        System.err.println(result);
    }
}