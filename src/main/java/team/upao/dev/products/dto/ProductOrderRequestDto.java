package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderRequestDto {
    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double unitPrice;

    @NotBlank
    private Double subtotal;

    @NotNull
    private UUID orderId;

    @NotNull
    private Long productId;
}
