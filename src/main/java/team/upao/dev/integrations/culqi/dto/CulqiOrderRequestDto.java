package team.upao.dev.integrations.culqi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CulqiOrderRequestDto {
    @NotNull
    @Min(600)
    @Max(50000)
    private Integer amount;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^(?i)[A-Z]{3}$")
    private String currencyIsoCode;

    @NotBlank
    @Size(min = 5, max = 80)
    private String description;

    @NotBlank
    @Size(min = 1, max = 36)
    private String orderNumber;

    @NotBlank
    private String expirationDate;

    @NotNull
    private Boolean confirm;

    @Valid
    @NotNull
    private ClientDetailsRequestDTO clientDetailsRequest;
}
