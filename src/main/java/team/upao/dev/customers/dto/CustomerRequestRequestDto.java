package team.upao.dev.customers.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.customers.enums.DocumentType;
import team.upao.dev.persons.dto.PersonRequestDto;

@Getter @Setter
public class CustomerRequestRequestDto extends PersonRequestDto {

    private String email;
    private String documentNumber;
    private String department;
    private String province;
    private String district;
    private String completeAddress;
}
