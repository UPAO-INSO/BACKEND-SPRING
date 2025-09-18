package team.upao.dev.products.service;

import team.upao.dev.common.service.BaseService;
import team.upao.dev.products.dto.ProductOrderRequestDto;
import team.upao.dev.products.dto.ProductOrderResponseDto;
import team.upao.dev.products.model.ProductOrderModel;

public interface ProductOrderService extends BaseService<ProductOrderRequestDto, ProductOrderResponseDto, Long> {
ProductOrderModel findModelById(Long id);
}
