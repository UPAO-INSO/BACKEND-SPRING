package upao.inso.dclassic.clients.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import upao.inso.dclassic.persons.dto.PersonDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientDto extends PersonDto {

    @NotBlank
    private String email;

    @NotBlank
    private String documentNumber;
}
