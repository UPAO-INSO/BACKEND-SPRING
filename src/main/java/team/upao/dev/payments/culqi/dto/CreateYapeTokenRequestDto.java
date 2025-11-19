package team.upao.dev.payments.culqi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateYapeTokenRequestDto {
    @NotNull(message = "El identificador del pedido es obligatorio")
    private Long orderId;

    @NotBlank(message = "El OTP proporcionado por Yape es obligatorio")
    @Size(min = 6, max = 6, message = "El OTP debe tener 6 dígitos")
    private String otp;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^9\\d{8}$", message = "El número de Yape debe iniciar con 9 y tener 9 dígitos")
    private String phoneNumber;
}
