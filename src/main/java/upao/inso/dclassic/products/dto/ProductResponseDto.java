package upao.inso.dclassic.products.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Long productTypeId;
    private String productTypeName;
    private Boolean active;
    private Boolean available;
}