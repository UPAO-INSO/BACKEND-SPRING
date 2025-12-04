package team.upao.dev.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductTypeRequestDto {
    @NotBlank
    private String name;
}
