package upao.inso.dclassic.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.products.dto.ProductOrderRequestDto;
import upao.inso.dclassic.products.dto.ProductOrderResponseDto;
import upao.inso.dclassic.products.model.ProductModel;
import upao.inso.dclassic.products.model.ProductOrderModel;

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
