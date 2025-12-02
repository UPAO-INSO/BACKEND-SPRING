package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Builder
public class ProductOrderResponseDto {
    private Long id;

    @NotBlank
    private Integer quantity;

    @NotBlank
    private Integer servedQuantity;

    @NotBlank
    private Double unitPrice;

    @NotBlank
    private Double subtotal;

    private String status;

    private UUID orderId;

    private Long productId;

    private String productName;

    private String productTypeName;
}
