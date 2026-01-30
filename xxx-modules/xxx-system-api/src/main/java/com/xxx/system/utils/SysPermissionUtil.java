//package com.xxx.system.utils;
//
//import com.xxx.common.core.utils.ObjectUtils;
//import com.xxx.system.vo.SysPermissionVO;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class SysPermissionUtil {
//
//    public static List<SysPermissionVO> buildTree(List<SysPermissionVO> voList) {
//        List<SysPermissionVO> treeVOList = new ArrayList<>();
//        List<Integer> tempList = voList.stream().map(SysPermissionVO::getId).toList();
//        for (SysPermissionVO vo : voList) {
//            // 如果是顶级节点, 遍历该父节点的所有子节点
//            if (!tempList.contains(vo.getParentId())) {
//                recursionBuild(voList, vo);
//                treeVOList.add(vo);
//            }
//        }
//        if (treeVOList.isEmpty()) {
//            treeVOList = voList;
//        }
//        return treeVOList;
//    }
//
//    private static void recursionBuild(List<SysPermissionVO> voList, SysPermissionVO vo) {
//        // 得到子节点列表
//        List<SysPermissionVO> childVOList = getChildList(voList, vo);
//        vo.setChildren(childVOList);
//        for (SysPermissionVO childVO : childVOList) {
//            if (hasChild(voList, childVO)) {
//                recursionBuild(voList, childVO);
//            }
//        }
//    }
//
//    private static boolean hasChild(List<SysPermissionVO> voList, SysPermissionVO vo) {
//        return !getChildList(voList, vo).isEmpty();
//    }
//
//    private static List<SysPermissionVO> getChildList(List<SysPermissionVO> voList, SysPermissionVO vo) {
//        List<SysPermissionVO> tempVOList = new ArrayList<>();
//        for (SysPermissionVO tempVO : voList) {
//            if (ObjectUtils.isNotNull(tempVO.getParentId()) && Objects.equals(tempVO.getParentId(), vo.getId())) {
//                tempVOList.add(tempVO);
//            }
//        }
//        return tempVOList;
//    }
//
//}
