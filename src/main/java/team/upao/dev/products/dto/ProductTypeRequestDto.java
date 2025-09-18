package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductTypeRequestDto {
    @NotBlank
    private String name;
}
