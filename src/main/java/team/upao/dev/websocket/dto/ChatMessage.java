package team.upao.dev.websocket.dto;

import lombok.*;
import team.upao.dev.websocket.enums.MessageType;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
}