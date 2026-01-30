package com.xxx.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 消息的通用封装类
 * 用于客户端和服务器之间传输所有类型的消息
 *
 * @param <T> 消息数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessageEnvelope<T> {
    private String type;      // 消息类型，对应 WebSocketMessageType 枚举的名称
    private T data;           // 消息的具体数据内容
    private String timestamp; // 消息发送时间戳，格式为 "yyyy-MM-dd HH:mm:ss"
}