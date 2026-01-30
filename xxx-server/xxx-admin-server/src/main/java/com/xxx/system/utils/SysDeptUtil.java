package com.xxx.system.utils;

import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.system.vo.SysDeptVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SysDeptUtil {

    public static List<SysDeptVO> buildTree(List<SysDeptVO> voList) {
        List<SysDeptVO> treeVOList = new ArrayList<>();
        List<Integer> tempList = voList.stream().map(SysDeptVO::getId).toList();
        for (SysDeptVO vo : voList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(vo.getParentId())) {
                recursionBuild(voList, vo);
                treeVOList.add(vo);
            }
        }
        if (treeVOList.isEmpty()) {
            treeVOList = voList;
        }
        return treeVOList;
    }

    private static void recursionBuild(List<SysDeptVO> voList, SysDeptVO vo) {
        // 得到子节点列表
        List<SysDeptVO> childVOList = getChildList(voList, vo);
        vo.setChildren(childVOList);
        for (SysDeptVO childVO : childVOList) {
            if (hasChild(voList, childVO)) {
                recursionBuild(voList, childVO);
            }
        }
    }

    private static boolean hasChild(List<SysDeptVO> voList, SysDeptVO vo) {
        return !getChildList(voList, vo).isEmpty();
    }

    private static List<SysDeptVO> getChildList(List<SysDeptVO> voList, SysDeptVO vo) {
        List<SysDeptVO> tempVOList = new ArrayList<>();
        for (SysDeptVO tempVO : voList) {
            if (ObjectUtils.isNotNull(tempVO.getParentId()) && Objects.equals(tempVO.getParentId(), vo.getId())) {
                tempVOList.add(tempVO);
            }
        }
        return tempVOList;
    }

}
