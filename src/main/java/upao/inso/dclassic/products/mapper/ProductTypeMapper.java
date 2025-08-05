package upao.inso.dclassic.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.products.dto.ProductTypeRequestDto;
import upao.inso.dclassic.products.dto.ProductTypeResponseDto;
import upao.inso.dclassic.products.model.ProductTypeModel;

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
