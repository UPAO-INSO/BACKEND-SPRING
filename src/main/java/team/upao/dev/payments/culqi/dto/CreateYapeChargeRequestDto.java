package team.upao.dev.payments.culqi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateYapeChargeRequestDto {
    @NotNull(message = "El identificador del pedido es obligatorio")
    private Long orderId;

    @NotBlank(message = "El token de Yape es obligatorio")
    private String tokenId;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo del cliente es obligatorio")
    private String email;

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;

    @NotBlank(message = "El teléfono del cliente es obligatorio")
    @Pattern(regexp = "^9\\d{8}$", message = "El número debe iniciar con 9 y tener 9 dígitos")
    private String phoneNumber;

    @Size(max = 80, message = "La dirección no debe exceder los 80 caracteres")
    private String address;

    @Size(max = 40, message = "La ciudad no debe exceder los 40 caracteres")
    private String city;

    @NotBlank(message = "El código de país es obligatorio")
    @Size(max = 2, message = "El código de país debe ser ISO-3166-1 alfa 2")
    private String countryCode = "PE";
}
