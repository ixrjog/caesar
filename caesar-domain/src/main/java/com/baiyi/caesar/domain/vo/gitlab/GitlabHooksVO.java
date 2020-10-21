package com.baiyi.caesar.domain.vo.gitlab;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/20 11:39 上午
 * @Version 1.0
 */
public class GitlabHooksVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Webhooks {
        public String ver = "1.0.0";
        private String object_kind;
        private String event_name;
        private String before;
        private String after;
        private String ref;
        private String checkout_sha;
        private int user_id;
        private String user_name;
        private String user_username;
        private String user_email;
        private String user_avatar;
        private int project_id;
        private Project project;
        private Repository repository;
        private List<Commits> commits;
        private int total_commits_count;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Project {
        private long id;
        private String name;
        private String description;
        private String web_url;
        private String avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Commits {
        private String id;
        private String message;
        private String timestamp;
        private String url;
        private List<String> added;
        private List<String> modified;
        private List<String> removed;
        private Author author;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Repository  {
        private String name;
        private String url;
        private String description;
        private String homepage;
        private String git_http_url;
        private String git_ssh_url;
        private int visibility_level;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Author {
        private String name;
        private String email;
    }


}
