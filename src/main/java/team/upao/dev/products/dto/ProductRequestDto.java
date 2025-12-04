package team.upao.dev.products.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    // Para platos (ENTRADAS, SEGUNDOS, CARTA) - lista de ingredientes
    private java.util.List<RecipeItemDto> recipe;
    
    // Para bebidas y descartables - cantidad inicial en inventario
    private BigDecimal initialQuantity;
}
