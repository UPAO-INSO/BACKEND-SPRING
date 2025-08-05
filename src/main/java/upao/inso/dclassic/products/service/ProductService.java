package upao.inso.dclassic.products.service;

import upao.inso.dclassic.common.service.BaseService;
import upao.inso.dclassic.products.dto.ProductRequestDto;
import upao.inso.dclassic.products.dto.ProductResponseDto;
import upao.inso.dclassic.products.model.ProductModel;

import java.util.List;

public interface ProductService extends BaseService<ProductRequestDto, ProductResponseDto, Long> {
    ProductResponseDto findByName(String name);
    List<ProductResponseDto> findByNameContaining(String name);
    ProductModel findModelById(Long id);
    boolean existsByName(String name);
    void updateNameById(Long id, String name);
    void updatePriceById(Long id, Double price);
    void updateDescriptionById(Long id, String description);
    void updateProductTypeById(Long id, Long productTypeId);
    void updateActiveById(Long id, Boolean active);
    void updateAvailableById(Long id, Boolean available);
}
