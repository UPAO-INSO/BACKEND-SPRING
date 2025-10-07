package team.upao.dev.websocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import team.upao.dev.websocket.dto.ChatMessage;

@Controller
public class WebSocketController {
    @MessageMapping("/sendMessage/{roomId}")

    @SendTo("/topic/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage chatMessage) {
        return ChatMessage.builder()
                .content(chatMessage.getContent())
                .sender(chatMessage.getSender())
                .build();
    }
}
