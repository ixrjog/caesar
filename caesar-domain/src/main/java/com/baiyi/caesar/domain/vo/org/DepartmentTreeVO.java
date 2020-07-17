package com.baiyi.caesar.domain.vo.org;

import com.baiyi.caesar.domain.vo.tree.TreeVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:32 下午
 * @Version 1.0
 */
public class DepartmentTreeVO {

    @Data
    @Builder
    @ApiModel
    public static class DepartmentTree {
        private Integer parentId;
        private List<TreeVO.DeptTree> tree;
    }
}
