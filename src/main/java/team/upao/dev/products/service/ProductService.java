package team.upao.dev.products.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.service.BaseService;
import team.upao.dev.products.dto.ProductRequestDto;
import team.upao.dev.products.dto.ProductResponseDto;
import team.upao.dev.products.model.ProductModel;

import java.util.List;

public interface ProductService extends BaseService<ProductRequestDto, ProductResponseDto, Long> {
    PaginationResponseDto<ProductResponseDto> findAllByProductTypeId(Long productTypeId, PaginationRequestDto requestDto);
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
