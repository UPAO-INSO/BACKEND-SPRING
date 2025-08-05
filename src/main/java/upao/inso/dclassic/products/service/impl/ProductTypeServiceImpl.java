package upao.inso.dclassic.products.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.exceptions.NotFoundException;
import upao.inso.dclassic.products.dto.ProductTypeRequestDto;
import upao.inso.dclassic.products.dto.ProductTypeResponseDto;
import upao.inso.dclassic.products.mapper.ProductTypeMapper;
import upao.inso.dclassic.products.model.ProductTypeModel;
import upao.inso.dclassic.products.repository.IProductTypeRepository;
import upao.inso.dclassic.products.service.ProductTypeService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    private final IProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;

    @Override
    public boolean existsByName(String nameType) {
        return productTypeRepository.existsByName(nameType);
    }

    @Override
    @Transactional
    public ProductTypeResponseDto create(ProductTypeRequestDto productTypeResponseDto) {
        ProductTypeModel productTypeModel = productTypeMapper.toModel(productTypeResponseDto);

        if (existsByName(productTypeModel.getName())) {
            throw new IllegalArgumentException("Product type with name: " + productTypeModel.getName() + " already exists");
        }

        productTypeRepository.save(productTypeModel);

        return productTypeMapper.toDto(productTypeModel);
    }

    @Override
    public PaginationResponseDto<ProductTypeResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductTypeModel> entities = productTypeRepository.findAll(pageable);
        final List<ProductTypeResponseDto> productTypeResponseDtos = productTypeMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                productTypeResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public ProductTypeResponseDto findById(Long id) {
        ProductTypeModel product = this.productTypeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product type not found with id: " + id));

        return productTypeMapper.toDto(product);
    }

    @Override
    public ProductTypeModel findModelById(Long id) {
        return this.productTypeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product type not found with id: " + id));
    }

    @Override
    public ProductTypeResponseDto findByName(String nameType) {
        ProductTypeModel productTypeModel = this.productTypeRepository
                .findByName(nameType)
                .orElseThrow(() -> new NotFoundException("Product type not found with name: " + nameType));

        return productTypeMapper.toDto(productTypeModel);
    }

    @Override
    public ProductTypeModel findModelByName(String nameType) {
        return this.productTypeRepository
                .findByName(nameType)
                .orElseThrow(() -> new NotFoundException("Product type not found with name: " + nameType));
    }

    @Override
    @Transactional
    public ProductTypeResponseDto update(Long id, ProductTypeRequestDto productTypeModel) {
        ProductTypeModel productType = findModelById(id);

        productType.setName(productTypeModel.getName());

        productTypeRepository.save(productType);

        return productTypeMapper.toDto(productType);
    }

    @Override
    @Transactional
    public ProductTypeResponseDto partialUpdate(Long id, ProductTypeResponseDto productType) {
        findModelById(id);

        if (productType.getName() != null && !productType.getName().isEmpty()) {
            updateNameById(id, productType.getName());
        }

        ProductTypeModel productTypeModel = findModelById(id);

        return productTypeMapper.toDto(productTypeModel);
    }

    @Override
    @Transactional
    public void updateNameById(Long id, String nameType) {
        findModelById(id);

        if (existsByName(nameType)) {
            throw new IllegalArgumentException("Product type with name: " + nameType + " already exists");
        }

        productTypeRepository.updateNameById(id, nameType);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        this.findById(id);
        return "Product type with id " + id + " deleted successfully.";
    }
}
