package team.upao.dev.products.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public PaginationResponseDto<ProductOrderResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductOrderModel> entities = productOrderRepository.findAll(pageable);
        final List<ProductOrderResponseDto> dtos = productOrderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                dtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional
    public ProductOrderResponseDto create(ProductOrderRequestDto productOrder) {
        ProductOrderModel model = productOrderMapper.toModel(productOrder);
        return productOrderMapper.toDto(productOrderRepository.save(model));
    }

    @Override
    @Transactional
    public ProductOrderResponseDto update(Long id, ProductOrderRequestDto dto) {
        ProductOrderModel existing = findModelById(id);
        if (dto.getQuantity() != null) existing.setQuantity(dto.getQuantity());
        if (dto.getUnitPrice() != null) existing.setUnitPrice(dto.getUnitPrice());
        if (dto.getSubtotal() != null) existing.setSubtotal(dto.getSubtotal());
        return productOrderMapper.toDto(productOrderRepository.save(existing));
    }

    @Override
    public ProductOrderResponseDto partialUpdate(Long id, ProductOrderResponseDto dto) {
        throw new UnsupportedOperationException("Use update() instead");
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOrderResponseDto findById(Long id) {
        return productOrderMapper.toDto(findModelById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOrderModel findModelById(Long id) {
        return productOrderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductOrder not found with id: " + id));
    }

    @Override
    @Transactional
    public String delete(Long id) {
        ProductOrderModel model = findModelById(id);
        productOrderRepository.delete(model);
        return String.format("Deleted ProductOrder with id: %s", id);
    }
}
