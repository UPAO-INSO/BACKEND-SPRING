package team.upao.dev.products.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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