package team.upao.dev.products.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.products.dto.ProductRequestDto;
import team.upao.dev.products.dto.ProductResponseDto;
import team.upao.dev.products.mapper.ProductMapper;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.model.ProductTypeModel;
import team.upao.dev.products.repository.IProductRepository;
import team.upao.dev.products.service.ProductService;
import team.upao.dev.products.service.ProductTypeService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final IProductRepository productRepository;
    private final ProductTypeService productTypeService;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponseDto> findByIds(List<Long> ids) {
        List<ProductModel> products = productRepository.findAllById(ids);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found with the provided IDs.");
        }

        return productMapper.toDto(products)
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
    }

    @Override
    public PaginationResponseDto<ProductResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductModel> entities = productRepository.findAll(pageable);
        final List<ProductResponseDto> productResponseDtos = productMapper.toDto(entities.getContent())
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
        return new PaginationResponseDto<>(
                productResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    public PaginationResponseDto<ProductResponseDto> findAllByProductTypeId(Long productTypeId, PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductModel> entities = productRepository.findAllByProductTypeId(productTypeId, pageable);
        final List<ProductResponseDto> productResponseDtos = productMapper.toDto(entities.getContent())
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
        return new PaginationResponseDto<>(
                productResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public ProductResponseDto findByName(String name) {
        ProductModel product = productRepository
                .findByNameIgnoreCase(name).stream().filter(p -> p.getActive() == true)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with name: " + name));

        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponseDto> findByNameContaining(String name) {
        List<ProductModel> products = productRepository
                .findByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("No products found containing name: " + name));

        return productMapper.toDto(products)
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
    }

    @Override
    public ProductResponseDto findById(Long id) {
        ProductModel product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toDto(product);
    }

    @Override
    public ProductModel findModelById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductResponseDto create(ProductRequestDto product) {
        if (productRepository.existsByName(product.getName()))
            throw new IllegalArgumentException("Product with name: " + product.getName() + " already exists");

        ProductModel productModel = productMapper.toModel(product);
        ProductTypeModel productType = productTypeService.findModelById(product.getProductTypeId());

        productModel.setProductType(productType);

        productRepository.save(productModel);

        return productMapper.toDto(productModel);
    }

    @Override
    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto product) {
        ProductModel productExisting = findModelById(id);

        productExisting.setName(product.getName());
        productExisting.setDescription(product.getDescription());
        productExisting.setPrice(product.getPrice());
        productExisting.setAvailable(product.getAvailable() != null ? product.getAvailable() : true);
        productExisting.setActive(product.getActive() != null ? product.getActive() : true);

        productRepository.save(productExisting);

        return productMapper.toDto(productExisting);
    }

    @Override
    @Transactional
    public ProductResponseDto partialUpdate(Long id, ProductResponseDto product) {
        findModelById(id);

        if (product.getName() != null && !product.getName().isBlank()) {
            updateNameById(id, product.getName());
        }

        if (product.getPrice() != null && product.getPrice() >= 0) {
            updatePriceById(id, product.getPrice());
        }

        if (product.getDescription() != null && !product.getDescription().isBlank()) {
            updateDescriptionById(id, product.getDescription());
        }

        if (product.getProductTypeId() != null) {
            Long productTypeId = productTypeService.findModelById(product.getProductTypeId()).getId();
            updateProductTypeById(id, productTypeId);
        }

        if (product.getActive() != null) {
            updateActiveById(id, product.getActive());
        }

        if (product.getAvailable() != null) {
            updateAvailableById(id, product.getAvailable());
        }

        ProductModel productExisting = findModelById(id);

        return productMapper.toDto(productExisting);
    }

    @Override
    @Transactional
    public void updateNameById(Long id, String name) {
        this.findById(id);

        if (existsByName(name)) {
            throw new IllegalArgumentException("Product with name: " + name + " already exists");
        }

        this.productRepository.updateNameById(id, name);
    }

    @Override
    @Transactional
    public void updatePriceById(Long id, Double price) {
        this.findById(id);

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.productRepository.updatePriceById(id, price);
    }

    @Override
    @Transactional
    public void updateDescriptionById(Long id, String description) {
        this.findById(id);

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        this.productRepository.updateDescriptionById(id, description);
    }

    @Override
    @Transactional
    public void updateProductTypeById(Long id, Long productTypeId) {
        this.findById(id);
        ProductTypeModel productType = productTypeService.findModelById(productTypeId);

        this.productRepository.updateProductTypeById(id, productType);
    }

    @Override
    @Transactional
    public void updateAvailableById(Long id, Boolean available) {
        this.findById(id);

        if (available == null) {
            throw new IllegalArgumentException("Available status cannot be null");
        }

        this.productRepository.updateAvailableById(id, available);
    }

    @Override
    @Transactional
    public void updateActiveById(Long id, Boolean active) {
        this.findById(id);

        if (active == null) {
            throw new IllegalArgumentException("Active status cannot be null");
        }

        this.productRepository.updateActiveById(id, active);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        ProductModel product = this.findModelById(id);

        product.setActive(false);

        productRepository.save(product);

        return "Product with id " + id + " has been marked as unavailable.";
    }
}
