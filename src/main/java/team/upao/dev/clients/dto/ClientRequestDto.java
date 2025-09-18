package team.upao.dev.clients.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import team.upao.dev.persons.dto.PersonDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientRequestDto extends PersonDto {

    @NotBlank
    private String email;

    @NotBlank
    private String documentNumber;
}
