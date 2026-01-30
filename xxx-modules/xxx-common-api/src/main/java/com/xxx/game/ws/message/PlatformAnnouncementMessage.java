package com.xxx.game.ws.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformAnnouncementMessage {
    private String title;   // 公告标题
    private String content; // 公告内容
    private String sender;  // 发布者 (例如: System Admin)
}