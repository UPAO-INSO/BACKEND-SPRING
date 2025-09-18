package team.upao.dev.products.service;

import team.upao.dev.common.service.BaseService;
import team.upao.dev.products.dto.ProductTypeRequestDto;
import team.upao.dev.products.dto.ProductTypeResponseDto;
import team.upao.dev.products.model.ProductTypeModel;

public interface ProductTypeService extends BaseService<ProductTypeRequestDto, ProductTypeResponseDto, Long> {
    boolean existsByName(String nameType);
    ProductTypeModel findModelById(Long id);
    ProductTypeResponseDto findByName(String nameType);
    ProductTypeModel findModelByName(String nameType);
    void updateNameById(Long id, String nameType);
}
