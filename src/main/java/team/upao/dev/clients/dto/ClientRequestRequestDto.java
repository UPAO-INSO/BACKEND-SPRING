package team.upao.dev.clients.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import team.upao.dev.persons.dto.PersonRequestDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientRequestRequestDto extends PersonRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String documentNumber;
}
