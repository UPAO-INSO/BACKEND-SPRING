package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ProductOrderRequestDto {
    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double unitPrice;

    @NotBlank
    private Double subtotal;

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;
}
