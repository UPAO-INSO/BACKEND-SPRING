package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductOrderRequestDto {
    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double unitPrice;

    @NotBlank
    private Double subtotal;

    private Long orderId;

    private Long productId;
}
