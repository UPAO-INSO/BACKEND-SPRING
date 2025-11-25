package team.upao.dev.clients.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ClientResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String phone;
    private String documentNumber;
    private String email;
}
