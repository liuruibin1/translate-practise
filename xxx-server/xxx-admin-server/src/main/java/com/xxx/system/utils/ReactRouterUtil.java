package com.xxx.system.utils;

import com.xxx.common.core.constants.HttpConstant;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.system.bo.ReactRouterBO;
import com.xxx.system.bo.ReactRouterMetaBO;
import com.xxx.system.constants.ReactRouterConstant;
import com.xxx.system.enumerate.PermissionTypeEnum;
import com.xxx.system.vo.SysPermissionVO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.xxx.system.constants.SysPermissionConstant.PERMISSION_ROOT_ID;

public class ReactRouterUtil {

    public static List<ReactRouterBO> buildTree(List<SysPermissionVO> voList) {
        List<ReactRouterBO> routers = new LinkedList<>();
        for (SysPermissionVO vo : voList) {
            ReactRouterBO reactRouter = new ReactRouterBO();
            reactRouter.setHidden(!vo.getIsVisible());
            reactRouter.setName(getRouterName(vo));
            reactRouter.setPath(getRouterPath(vo));
            reactRouter.setComponent(getComponent(vo));
            reactRouter.setQuery(vo.getUrlQuery());
            reactRouter.setCode(vo.getCode());
            //
            ReactRouterMetaBO reactRouterMeta = ReactRouterMetaBO.builder()
                    .title(vo.getName())
                    .icon(vo.getIcon())
                    .noCache(vo.getIsCache())
                    .link(vo.getUrlPath())
                    .build();
            reactRouter.setMeta(reactRouterMeta);
            List<SysPermissionVO> tempVOList = vo.getChildren();
            if (tempVOList != null && !tempVOList.isEmpty() && PermissionTypeEnum.FOLDER.getValue().equals(vo.getType())) {
                reactRouter.setAlwaysShow(true);
                reactRouter.setRedirect("noRedirect");
                reactRouter.setChildren(buildTree(tempVOList));
            } else if (isPageFrame(vo)) {
                reactRouter.setMeta(null);
                List<ReactRouterBO> childrenList = new ArrayList<>();
                ReactRouterBO children = new ReactRouterBO();
                children.setPath(vo.getUrlPath());
                children.setComponent(vo.getPageComponent());
                children.setCode(vo.getCode());
                children.setName(StringUtils.capitalize(vo.getUrlPath()));
                //
                ReactRouterMetaBO tempReactRouterMeta = ReactRouterMetaBO.builder()
                        .title(vo.getName())
                        .icon(vo.getIcon())
                        .noCache(vo.getIsCache())
                        .link(vo.getUrlPath())
                        .build();
                children.setMeta(tempReactRouterMeta);
                children.setQuery(vo.getUrlQuery());
                childrenList.add(children);
                reactRouter.setChildren(childrenList);
            } else if (vo.getParentId().equals(PERMISSION_ROOT_ID) && isInnerLink(vo)) {
                //
                ReactRouterMetaBO tempReactRouterMeta = ReactRouterMetaBO.builder()
                        .title(vo.getName())
                        .icon(vo.getIcon())
                        .build();
                reactRouter.setMeta(tempReactRouterMeta);
                reactRouter.setPath("/");
                List<ReactRouterBO> childrenList = new ArrayList<>();
                ReactRouterBO children = new ReactRouterBO();
                String routerPath = innerLinkReplaceEach(vo.getUrlPath());
                children.setPath(routerPath);
                children.setComponent(ReactRouterConstant.INNER_LINK);
                children.setCode(vo.getCode());
                children.setName(StringUtils.capitalize(routerPath));
                //
                ReactRouterMetaBO tempChildrenReactRouterMeta = ReactRouterMetaBO.builder()
                        .title(vo.getName())
                        .icon(vo.getUrlPath())
                        .build();
                children.setMeta(tempChildrenReactRouterMeta);
                childrenList.add(children);
                reactRouter.setChildren(childrenList);
            }
            routers.add(reactRouter);
        }
        return routers;
    }

    /**
     * 获取组件信息
     */
    public static String getComponent(SysPermissionVO vo) {
        String component = ReactRouterConstant.LAYOUT;
        if (StringUtils.isNotEmpty(vo.getPageComponent()) && !isPageFrame(vo)) {
            component = vo.getPageComponent();
        } else if (StringUtils.isEmpty(vo.getPageComponent())
                && !vo.getParentId().equals(PERMISSION_ROOT_ID)
                && isInnerLink(vo)) {
            component = ReactRouterConstant.INNER_LINK;
        } else if (StringUtils.isEmpty(vo.getPageComponent()) && isParentView(vo)) {
            component = ReactRouterConstant.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     *
     * @param vo 菜单信息
     * @return 结果
     */
    public static boolean isParentView(SysPermissionVO vo) {
        return !vo.getParentId().equals(PERMISSION_ROOT_ID)
                && PermissionTypeEnum.FOLDER.getValue().equals(vo.getType());
    }

    /**
     * 获取路由名称
     */
    private static String getRouterName(SysPermissionVO vo) {
        String routerName = StringUtils.capitalize(vo.getUrlPath());
        // 非外链并且是一级目录（类型为目录）
        if (isPageFrame(vo)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     */
    public static String getRouterPath(SysPermissionVO vo) {
        String routerPath = vo.getUrlPath();
        // 内链打开外网方式
        if (!vo.getParentId().equals(PERMISSION_ROOT_ID) && isInnerLink(vo)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (PERMISSION_ROOT_ID.equals(vo.getParentId())
                && PermissionTypeEnum.FOLDER.getValue().equals(vo.getType())
                && vo.getIsPageFrame()) {
            routerPath = "/" + vo.getUrlPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isPageFrame(vo)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为菜单内部跳转
     */
    private static boolean isPageFrame(SysPermissionVO vo) {
        return PERMISSION_ROOT_ID.equals(vo.getParentId())
                && PermissionTypeEnum.PAGE.getValue().equals(vo.getType())
                && vo.getIsPageFrame();
    }

    /**
     * 是否为内链组件
     */
    public static boolean isInnerLink(SysPermissionVO vo) {
        return vo.getIsPageFrame() && StringUtils.isHttp(vo.getUrlPath());
    }

    /**
     * 内链域名特殊字符替换
     */
    public static String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(
                path,
                new String[]{HttpConstant.HTTP, HttpConstant.HTTPS, HttpConstant.WWW, "."},
                new String[]{"", "", "", "/"}
        );
    }

}
