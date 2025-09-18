package team.upao.dev.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.products.dto.ProductOrderRequestDto;
import team.upao.dev.products.dto.ProductOrderResponseDto;
import team.upao.dev.products.model.ProductOrderModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductOrderMapper {
    public ProductOrderModel toModel(ProductOrderRequestDto productOrderRequestDto) {
        return ProductOrderModel.builder()
                .quantity(productOrderRequestDto.getQuantity())
                .unitPrice(productOrderRequestDto.getUnitPrice())
                .subtotal(productOrderRequestDto.getQuantity() * productOrderRequestDto.getUnitPrice())
                .order(null)
                .product(null)
                .build();
    }

    public List<ProductOrderModel> toModel(List<ProductOrderRequestDto> productOrderRequestDtos) {
        return productOrderRequestDtos.stream()
                .map(this::toModel)
                .toList();
    }

    public ProductOrderResponseDto toDto(ProductOrderModel productOrderModel) {
        return ProductOrderResponseDto.builder()
                .id(productOrderModel.getId())
                .quantity(productOrderModel.getQuantity())
                .unitPrice(productOrderModel.getUnitPrice())
                .subtotal(productOrderModel.getSubtotal())
                .orderId(productOrderModel.getOrder().getId())
                .productId(productOrderModel.getProduct().getId())
                .build();
    }

    public List<ProductOrderResponseDto> toDto(List<ProductOrderModel> productOrderModels) {
        return productOrderModels.stream()
                .map(this::toDto)
                .toList();
    }
}
