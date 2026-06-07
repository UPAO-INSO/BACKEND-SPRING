package team.upao.dev.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Evento WebSocket para cambios de disponibilidad de productos.
 * Publicado en /topic/products
 */
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsProductEvent {

    /** PRODUCT_AVAILABILITY */
    private String type;

    private Long productId;
    private String productName;
    private Boolean available;

    @Builder.Default
    private String timestamp = Instant.now().toString();
}
