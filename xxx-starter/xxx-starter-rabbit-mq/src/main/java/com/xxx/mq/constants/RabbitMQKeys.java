package com.xxx.mq.constants;

public interface RabbitMQKeys {

    /**
     * 平台公告交换机 (Fanout Exchange)
     */
    String PLATFORM_ANNOUNCEMENT_EXCHANGE = "platform.announcement.exchange";

    /**
     * WebSocket 内部广播交换机 (Fanout Exchange)
     */
    String WEBSOCKET_BROADCAST_EXCHANGE = "websocket.broadcast.exchange";

    /**
     * 游戏事件消息交换机
     */
    String GAME_EVENT_EXCHANGE = "game.event_message.exchange";

    /**
     * 游戏回合信息 队列
     */
    String GAME_ROUND_QUEUE = "game.round.queue";

    /**
     * 游戏回合信息 路由
     */
    String GAME_ROUND_ROUTING = "game.round.routing";

    /**
     * 游戏回合赢家信息 队列
     */
    String GAME_ROUND_WINNER_QUEUE = "game.round.winner.queue";

    /**
     * 游戏回合赢家信息 路由
     */
    String GAME_ROUND_WINNER_ROUTING = "game.round.winner.routing";

    /**
     * 游戏回合爆点滴答 队列
     */
    String GAME_ROUND_CRASH_TICK_QUEUE = "game.round.crash_tick.queue";

    /**
     * 游戏回合爆点滴答 路由
     */
    String GAME_ROUND_CRASH_TICK_ROUTING = "game.round.crash_tick.routing";

    ///**
    // * 游戏回合结算 队列
    // */
    //String GAME_ROUND_SETTLEMENT_QUEUE = "game.round.settlement.queue";

    ///**
    // * 游戏回合结算 路由
    // */
    //String GAME_ROUND_SETTLEMENT_ROUTING = "game.round.settlement.routing";

}