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

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Complete address is required")
    private String completeAddress;
}
