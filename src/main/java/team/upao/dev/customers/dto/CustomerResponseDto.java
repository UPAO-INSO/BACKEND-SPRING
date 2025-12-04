package team.upao.dev.customers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.customers.enums.DocumentType;

@Getter @Setter
@Builder
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String phone;
    private String documentNumber;
    private DocumentType documentType;
    private String email;
    private String departament;
    private String province;
    private String district;
    private String completeAddress;
}
