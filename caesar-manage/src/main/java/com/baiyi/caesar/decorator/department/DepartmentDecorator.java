package com.baiyi.caesar.decorator.department;

import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartment;
import com.baiyi.caesar.domain.vo.org.DepartmentTreeVO;
import com.baiyi.caesar.domain.vo.tree.TreeVO;
import com.baiyi.caesar.facade.OrgFacade;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:47 下午
 * @Version 1.0
 */
@Component
public class DepartmentDecorator {

    @Resource
    private OrgFacade orgFacade;

    public List<TreeVO.DeptTree> deptListToTree(List<OcOrgDepartment> deptList) {
        if (CollectionUtils.isEmpty(deptList))
            return null;
        List<TreeVO.DeptTree> treeList = Lists.newArrayList();
        deptList.parallelStream().forEach(e -> {
            TreeVO.DeptTree tree = TreeVO.DeptTree.builder()
                    .id(e.getId())
                    .label(e.getName())
                    .build();
            assembleChildren(tree);
            treeList.add(tree);
        });
        return treeList;
    }

    /**
     * 组织架构递归算法
     *
     * @param tree
     */
    private void assembleChildren(TreeVO.DeptTree tree) {
        DepartmentTreeVO.DepartmentTree departmentTree = orgFacade.queryDepartmentTree(tree.getId());
        if (departmentTree != null && departmentTree.getTree() != null) {
            departmentTree.getTree().parallelStream().forEach(this::assembleChildren);
            tree.setChildren(departmentTree.getTree());
        }
    }

}
