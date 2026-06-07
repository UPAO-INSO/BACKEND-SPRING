package team.upao.dev.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Evento WebSocket para cambios de estado de mesas.
 * Publicado en /topic/tables
 */
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsTableEvent {

    /** TABLE_STATUS_CHANGED */
    private String type;

    private Long tableId;
    private String tableStatus;   // AVAILABLE | OCCUPIED | RESERVED

    @Builder.Default
    private String timestamp = Instant.now().toString();
}
