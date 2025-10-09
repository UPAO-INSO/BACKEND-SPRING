package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive
    private Double price;

    @NotNull(message = "Product type ID is required")
    private Long productTypeId;

    private Boolean active;

    private Boolean available;
}
