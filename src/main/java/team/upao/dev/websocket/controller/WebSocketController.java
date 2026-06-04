package team.upao.dev.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import team.upao.dev.websocket.dto.ChatMessage;
import team.upao.dev.websocket.dto.WsProductEvent;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /** Retransmite un mensaje de sala a todos los suscriptores del topic. */
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage chatMessage) {
        return ChatMessage.builder()
                .content(chatMessage.getContent())
                .sender(chatMessage.getSender())
                .build();
    }

    /**
     * Recibe una actualización de disponibilidad de producto desde cualquier
     * cliente y la retransmite a /topic/products para que todos se actualicen.
     */
    @MessageMapping("/product-update")
    @SendTo("/topic/products")
    public WsProductEvent relayProductUpdate(WsProductEvent event) {
        return event;
    }
}
