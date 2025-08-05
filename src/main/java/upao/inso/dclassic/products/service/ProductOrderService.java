package upao.inso.dclassic.products.service;

import upao.inso.dclassic.common.service.BaseService;
import upao.inso.dclassic.products.dto.ProductOrderRequestDto;
import upao.inso.dclassic.products.dto.ProductOrderResponseDto;
import upao.inso.dclassic.products.model.ProductOrderModel;

public interface ProductOrderService extends BaseService<ProductOrderRequestDto, ProductOrderResponseDto, Long> {
ProductOrderModel findModelById(Long id);
}
