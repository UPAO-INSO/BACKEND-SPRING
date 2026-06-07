package team.upao.dev.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Evento WebSocket para cambios relacionados con pedidos.
 * Publicado en /topic/orders
 */
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsOrderEvent {

    /** Tipo de evento */
    private String type;   // ORDER_CREATED | ORDER_UPDATED | ORDER_STATUS_CHANGED | PRODUCT_SERVED

    private String orderId;
    private Integer tableId;
    private String orderStatus;

    @Builder.Default
    private String timestamp = Instant.now().toString();
}
