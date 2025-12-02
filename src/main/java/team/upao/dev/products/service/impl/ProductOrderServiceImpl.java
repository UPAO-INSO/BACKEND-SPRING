package team.upao.dev.products.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.products.dto.ProductOrderRequestDto;
import team.upao.dev.products.dto.ProductOrderResponseDto;
import team.upao.dev.products.mapper.ProductOrderMapper;
import team.upao.dev.products.model.ProductOrderModel;
import team.upao.dev.products.repository.IProductOrderRepository;
import team.upao.dev.products.service.ProductOrderService;

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
        ProductOrderModel productOrderModel = this.findModelById(id);

        return productOrderMapper.toDto(productOrderModel);
    }

    @Override
    public ProductOrderModel findModelById(Long id) {
        return this.productOrderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Order with id: " + id));
    }

    @Override
    public String delete(Long id) {
        this.findById(id);
        return String.format("Delete Product Order with id: %s", id);
    }
}
