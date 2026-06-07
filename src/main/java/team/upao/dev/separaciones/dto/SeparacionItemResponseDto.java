package team.upao.dev.separaciones.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class SeparacionItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productType;
    private Integer quantity;
    private Double unitPrice;
}
