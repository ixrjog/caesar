package com.baiyi.caesar.domain.vo.tree;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/31 5:52 下午
 * @Version 1.0
 */
public class EngineVO implements Serializable {

    private static final long serialVersionUID = 576862193968765674L;

    @Data
    @Builder
    @ApiModel
    public static class Children implements Serializable {
        private static final long serialVersionUID = 7258295101670199717L;
        private String name;
        @Builder.Default
        private Integer value = 0;
        @Builder.Default
        private List<Children> children = Lists.newArrayList();

        public void addChildren(Children children) {
            this.children.add(children);
        }

    }
}
