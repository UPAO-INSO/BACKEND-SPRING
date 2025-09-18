package team.upao.dev.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.products.dto.ProductTypeRequestDto;
import team.upao.dev.products.dto.ProductTypeResponseDto;
import team.upao.dev.products.model.ProductTypeModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductTypeMapper {
    public ProductTypeModel toModel(ProductTypeRequestDto productTypeResponseDto) {
        return ProductTypeModel.builder()
                .name(productTypeResponseDto.getName())
                .build();
    }

    public ProductTypeResponseDto toDto(ProductTypeModel productModel) {
        return ProductTypeResponseDto.builder()
                .id(productModel.getId())
                .name(productModel.getName())
                .build();
    }

    public List<ProductTypeResponseDto> toDto(List<ProductTypeModel> productsType) {
        return productsType.stream()
                .map(this::toDto)
                .toList();
    }
}
