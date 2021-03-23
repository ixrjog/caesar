package com.baiyi.caesar.jenkins.handler;

import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.jenkins.http.Authentication;
import com.baiyi.caesar.jenkins.http.HttpUtil;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/3/23 1:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsBlueHandler {

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private StringEncryptor stringEncryptor;


    public void test1() {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceByName("master-1");
        Authentication authentication = Authentication.FREE;
        String api = Joiner.on("/").join(csJenkinsInstance.getUrl(),
                "blue/rest/jwt-auth/token");
        try {
            JsonNode jn = HttpUtil.httpGetExecutor(api, Maps.newHashMap(), authentication);
            System.err.println(jn.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void test2() {

        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceByName("master-1");
        Authentication authentication = Authentication.builder()
                .header(Authentication.Header.AUTHENTICATION)
                .token("Bearer " + stringEncryptor.decrypt(csJenkinsInstance.getToken()))
                .build();
        String api = Joiner.on("/").join(csJenkinsInstance.getUrl(),
                "blue/rest/organizations/jenkins/pipelines/ACCOUNT_account-server-build-prod/runs/1/nodes/");
        try {
            JsonNode jn = HttpUtil.httpGetExecutor(api, Maps.newHashMap(), authentication);
            System.err.println(jn.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
