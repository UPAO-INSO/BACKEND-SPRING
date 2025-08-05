package upao.inso.dclassic.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.products.dto.ProductRequestDto;
import upao.inso.dclassic.products.dto.ProductResponseDto;
import upao.inso.dclassic.products.model.ProductModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    public ProductModel toModel(ProductRequestDto dto) {
        return ProductModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .productType(null) // ProductType will be set later
                .active(dto.getActive() != null ? dto.getActive() : true)
                .available(dto.getAvailable() != null ? dto.getAvailable() : true)
                .build();
    }

    public List<ProductModel> toModel(List<ProductRequestDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .toList();
    }

    public ProductResponseDto toDto(ProductModel product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .productTypeId(product.getProductType().getId())
                .productTypeName(product.getProductType().getName())
                .active(product.getActive())
                .available(product.getAvailable())
                .build();
    }

    public List<ProductResponseDto> toDto(List<ProductModel> products) {
        return products.stream()
                .map(this::toDto)
                .toList();
    }
}
