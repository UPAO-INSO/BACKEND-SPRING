package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ProductOrderResponseDto {
    private Long id;

    @NotBlank
    private Integer quantity;

    @NotBlank
    private Double unitPrice;

    @NotBlank
    private Double subtotal;

    private Long orderId;

    private Long productId;

    private String productName;
}
