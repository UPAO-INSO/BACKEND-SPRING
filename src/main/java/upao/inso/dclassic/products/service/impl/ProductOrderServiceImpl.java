package upao.inso.dclassic.products.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.products.dto.ProductOrderRequestDto;
import upao.inso.dclassic.products.dto.ProductOrderResponseDto;
import upao.inso.dclassic.products.mapper.ProductOrderMapper;
import upao.inso.dclassic.products.model.ProductOrderModel;
import upao.inso.dclassic.products.repository.IProductOrderRepository;
import upao.inso.dclassic.products.service.ProductOrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService {
    private final IProductOrderRepository productOrderRepository;
    private final ProductOrderMapper productOrderMapper;

    @Override
    public PaginationResponseDto<ProductOrderResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductOrderModel> entities = productOrderRepository.findAll(pageable);
        final List<ProductOrderResponseDto> productOrderRequestDtos = productOrderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                productOrderRequestDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public ProductOrderResponseDto create(ProductOrderRequestDto productOrder) {
        ProductOrderModel productOrderModel = productOrderMapper.toModel(productOrder);
        return productOrderMapper.toDto(productOrderModel);
    }

    @Override
    public ProductOrderResponseDto update(Long id, ProductOrderRequestDto productOrderRequestDto) {
        return null;
    }

    @Override
    public ProductOrderResponseDto partialUpdate(Long aLong, ProductOrderResponseDto dto) {
        return null;
    }

    @Override
    public ProductOrderResponseDto findById(Long id) {
        return null;
    }

    @Override
    public ProductOrderModel findModelById(Long id) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return "";
    }
}
