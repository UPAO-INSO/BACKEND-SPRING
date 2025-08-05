package upao.inso.dclassic.clients.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ClientResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String phone;
    private String documentNumber;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
}
