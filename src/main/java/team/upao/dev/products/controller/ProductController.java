package team.upao.dev.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.controller.BaseController;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.service.BaseService;
import team.upao.dev.products.dto.ProductRequestDto;
import team.upao.dev.products.dto.ProductResponseDto;
import team.upao.dev.products.service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController extends BaseController<ProductRequestDto, ProductResponseDto, Long> {
    private final ProductService productService;

    @Override
    protected BaseService<ProductRequestDto, ProductResponseDto, Long> getService() {
        return productService;
    }

    @PostMapping("/find-by-ids")
    public ResponseEntity<List<ProductResponseDto>> findByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(productService.findByIds(ids));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponseDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/name-contains/{name}")
    public ResponseEntity<List<ProductResponseDto>> findByNameContaining(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByNameContaining(name));
    }

    @GetMapping("/by-product-type/{productTypeId}")
    public ResponseEntity<PaginationResponseDto<ProductResponseDto>> findAllByProductTypeId(
            @PathVariable Long productTypeId, @ModelAttribute PaginationRequestDto requestDto) {
        return ResponseEntity.ok(productService.findAllByProductTypeId(productTypeId, requestDto));
    }

    /**
     * Sincroniza las imágenes de productos con S3.
     * Lista los objetos bajo "products/" en el bucket, actualiza imageUrl en la BD
     * para cada producto cuyo slug coincida con un archivo en S3.
     */
    @PostMapping("/sync-images")
    public ResponseEntity<Map<String, Object>> syncImagesFromS3() {
        int updated = productService.syncImagesFromS3();
        return ResponseEntity.ok(Map.of(
                "message", "Sincronización completada",
                "updatedProducts", updated));
    }
}
