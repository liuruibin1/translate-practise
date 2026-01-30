package com.xxx.game.ws.constants;

/**
 * Game Redis 常量
 */
public interface WebSocketRedisKeys {

    /**
     * 平台在线用户数
     */
    String PLATFORM_ONLINE_USERS_COUNT = "platform:online:users:count";

    /**
     * 平台用户列表：platform -> Set<userId>
     */
    String PLATFORM_USER_LIST = "platform:user_list:";

}