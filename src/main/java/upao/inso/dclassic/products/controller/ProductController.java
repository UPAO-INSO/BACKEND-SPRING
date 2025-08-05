package upao.inso.dclassic.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.controller.BaseController;
import upao.inso.dclassic.common.service.BaseService;
import upao.inso.dclassic.products.dto.ProductRequestDto;
import upao.inso.dclassic.products.dto.ProductResponseDto;
import upao.inso.dclassic.products.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController extends BaseController<ProductRequestDto, ProductResponseDto, Long> {
    private final ProductService productService;

    @Override
    protected BaseService<ProductRequestDto, ProductResponseDto, Long> getService() {
        return productService;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponseDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/name-contains/{name}")
    public ResponseEntity<List<ProductResponseDto>> findByNameContaining(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByNameContaining(name));
    }
}
