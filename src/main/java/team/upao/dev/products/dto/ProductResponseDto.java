package team.upao.dev.products.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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