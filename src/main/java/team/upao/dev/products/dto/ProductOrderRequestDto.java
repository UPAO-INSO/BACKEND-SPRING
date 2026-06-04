package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderRequestDto {
    @NotNull
    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;

    private UUID orderId;

    @NotNull
    private Long productId;
}
