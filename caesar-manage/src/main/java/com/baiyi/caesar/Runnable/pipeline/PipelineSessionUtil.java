package com.baiyi.caesar.Runnable.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/4/2 11:04 上午
 * @Version 1.0
 */
public class PipelineSessionUtil {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PipelineContext {
        private String username;
        private Integer buildType;
        private String queryType;
        private Integer querySize;
    }

    private static Map<String, PipelineContext> userSessionsMap = new ConcurrentHashMap<>();

    public static void setUserSession(String sessionId, PipelineSessionUtil.PipelineContext context) {
        userSessionsMap.put(sessionId, context);
    }

    public static void removeUserSession(String sessionId) {
        userSessionsMap.remove(sessionId);
    }

    public static PipelineContext getUserBySessionId(String sessionId) {
        if (userSessionsMap.containsKey(sessionId))
            return userSessionsMap.get(sessionId);
        return null;
    }

}
