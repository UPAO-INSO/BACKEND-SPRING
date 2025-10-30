package team.upao.dev.clients.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.persons.dto.PersonRequestDto;

@Getter @Setter
public class ClientRequestRequestDto extends PersonRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String documentNumber;
}
