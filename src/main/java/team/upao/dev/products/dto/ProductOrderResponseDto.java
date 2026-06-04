package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductOrderResponseDto {
    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer servedQuantity;

    @NotNull
    private Double unitPrice;

    @NotNull
    private Double subtotal;

    private String status;

    private UUID orderId;

    private Long productId;

    private String productName;

    private String productTypeName;
}
