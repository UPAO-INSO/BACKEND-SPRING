package upao.inso.dclassic.products.service;

import upao.inso.dclassic.common.service.BaseService;
import upao.inso.dclassic.products.dto.ProductTypeRequestDto;
import upao.inso.dclassic.products.dto.ProductTypeResponseDto;
import upao.inso.dclassic.products.model.ProductTypeModel;

public interface ProductTypeService extends BaseService<ProductTypeRequestDto, ProductTypeResponseDto, Long> {
    boolean existsByName(String nameType);
    ProductTypeModel findModelById(Long id);
    ProductTypeResponseDto findByName(String nameType);
    ProductTypeModel findModelByName(String nameType);
    void updateNameById(Long id, String nameType);
}
