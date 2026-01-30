package com.xxx.mq.configuration;

import com.xxx.mq.constants.RabbitMQKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitMQ 配置类
 * 定义队列、交换机和绑定关系
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public FanoutExchange platformAnnouncementExchange() {
        // durable: true, autoDelete: false
        return new FanoutExchange(RabbitMQKeys.PLATFORM_ANNOUNCEMENT_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange websocketBroadcastExchange() {
        // durable: true, autoDelete: false
        return new FanoutExchange(RabbitMQKeys.WEBSOCKET_BROADCAST_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange gameEventExchange() {
        // durable: true, autoDelete: false
        return new DirectExchange(RabbitMQKeys.GAME_EVENT_EXCHANGE, true, false);
    }

    @Bean
    public Queue gameRoundQueue() {
        // durable: true 持久化队列
        return new Queue(RabbitMQKeys.GAME_ROUND_QUEUE, true);
    }

    @Bean
    public Binding bindingGameRoundRouting(Queue gameRoundQueue, DirectExchange gameEventExchange) {
        return BindingBuilder
                .bind(gameRoundQueue)
                .to(gameEventExchange)
                .with(RabbitMQKeys.GAME_ROUND_ROUTING);
    }

    @Bean
    public Queue gameRoundWinnerQueue() {
        // durable: true 持久化队列
        return new Queue(RabbitMQKeys.GAME_ROUND_WINNER_QUEUE, true);
    }

    @Bean
    public Binding bindingGameRoundWinnerRouting(Queue gameRoundWinnerQueue, DirectExchange gameEventExchange) {
        return BindingBuilder
                .bind(gameRoundWinnerQueue)
                .to(gameEventExchange)
                .with(RabbitMQKeys.GAME_ROUND_WINNER_ROUTING);
    }

    @Bean
    public Queue gameRoundCrashTickQueue() {
        // durable: true 持久化队列
        return new Queue(RabbitMQKeys.GAME_ROUND_CRASH_TICK_QUEUE, true);
    }

    @Bean
    public Binding bindingGameRoundCrashTickRouting(Queue gameRoundCrashTickQueue, DirectExchange gameEventExchange) {
        return BindingBuilder
                .bind(gameRoundCrashTickQueue)
                .to(gameEventExchange)
                .with(RabbitMQKeys.GAME_ROUND_CRASH_TICK_ROUTING);
    }

    //@Bean
    //public Queue gameRoundSettlementQueue() {
    //    // durable: true 持久化队列
    //    return new Queue(RabbitMQKeys.GAME_ROUND_SETTLEMENT_QUEUE, true);
    //}

    //@Bean
    //public Binding bindingGameRoundSettlementRouting(Queue gameRoundSettlementQueue, DirectExchange gameEventExchange) {
    //    return BindingBuilder
    //            .bind(gameRoundSettlementQueue)
    //            .to(gameEventExchange)
    //            .with(RabbitMQKeys.GAME_ROUND_SETTLEMENT_ROUTING);
    //}

}