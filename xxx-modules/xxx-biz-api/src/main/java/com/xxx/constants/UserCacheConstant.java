package com.xxx.constants;

public interface UserCacheConstant {

    /**
     * 用户登录后数据
     * cache object
     */
    String USER_LOGGED_DATA = "user:logged_data:";

    /**
     * 登录用户(父映射)
     * cache hash map
     */
    String USER_PARENT_MAP_FOR_LOGIN = "user:parent_map_for_login:";

    /**
     * 注册用户(父映射)
     * cache hash map
     */
    String USER_PARENT_MAP_FOR_REGISTER = "user:parent_map_for_register:";

    /**
     * (今日)用户注册数
     * cache set
     */
    String USER_REGISTER_TODAY_COUNT = "user:today_register_count:";

    /**
     * (今日)用户登录
     * cache set
     */
    String USER_LOGIN_TODAY = "user:login_today:";

}