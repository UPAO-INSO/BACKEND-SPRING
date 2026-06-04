package team.upao.dev.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.products.dto.ProductOrderRequestDto;
import team.upao.dev.products.dto.ProductOrderResponseDto;
import team.upao.dev.products.model.ProductOrderModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductOrderMapper {
    public ProductOrderModel toModel(ProductOrderRequestDto productOrderRequestDto) {
        Double unitPrice = productOrderRequestDto.getUnitPrice();
        Integer quantity = productOrderRequestDto.getQuantity();
        Double subtotal = (unitPrice != null && quantity != null)
                ? quantity.doubleValue() * unitPrice
                : (productOrderRequestDto.getSubtotal() != null ? productOrderRequestDto.getSubtotal() : 0.0);

        return ProductOrderModel.builder()
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
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
                .servedQuantity(
                        productOrderModel.getServedQuantity() != null ? productOrderModel.getServedQuantity() : 0)
                .unitPrice(productOrderModel.getUnitPrice())
                .subtotal(productOrderModel.getSubtotal())
                .status(productOrderModel.getStatus() != null ? productOrderModel.getStatus().name()
                        : OrderStatus.PENDING.name())
                .orderId(productOrderModel.getOrder().getId())
                .productId(productOrderModel.getProduct().getId())
                .productName(productOrderModel.getProduct().getName())
                .productTypeName(productOrderModel.getProduct().getProductType().getName())
                .build();
    }

    public List<ProductOrderResponseDto> toDto(List<ProductOrderModel> productOrderModels) {
        return productOrderModels.stream()
                .map(this::toDto)
                .toList();
    }
}
