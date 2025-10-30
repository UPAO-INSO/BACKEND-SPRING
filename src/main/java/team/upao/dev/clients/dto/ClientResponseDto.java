package team.upao.dev.clients.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
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
