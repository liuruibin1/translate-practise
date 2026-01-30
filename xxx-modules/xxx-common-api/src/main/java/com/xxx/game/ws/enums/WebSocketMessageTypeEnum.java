package com.xxx.game.ws.enums;

/**
 * WebSocket 消息类型枚举
 */
public enum WebSocketMessageTypeEnum {
    ROOM_INFO_MESSAGE,              // 房间信息消息

    INACTIVE_USER_KICKED_MESSAGE,   // 不活跃用户踢出消息

    USER_ENTER_ROOM_EVENT,          // 用户进入房间事件
    USER_ENTER_ROOM_MESSAGE,        // 用户进入房间消息

    USER_LEAVE_ROOM_EVENT,          // 用户离开房间事件
    USER_LEAVE_ROOM_MESSAGE,        // 用户离开房间消息

    USER_BET_EVENT,                 // 用户投注事件
    USER_BET_MESSAGE,               // 用户投注消息
    USER_BET_LIST_MESSAGE,          // 用户投注列表消息

    USER_CASHOUT_EVENT,             // 用户兑现事件
    USER_CASHOUT_MESSAGE,           // 用户兑现消息

    GAME_ROUND_MESSAGE,             // 游戏回合消息
    GAME_ROUND_WINNER_MESSAGE,      // 游戏回合赢家消息
    GAME_ROUND_CRASH_TICK_MESSAGE,  // 游戏回合爆点滴答消息

    PLATFORM_ANNOUNCEMENT_MESSAGE,  // 平台公告消息
    ERROR_MESSAGE,                  // 错误消息

    PING_MESSAGE,                   // Ping心跳消息
    PONG_MESSAGE                    // Pong心跳消息

}