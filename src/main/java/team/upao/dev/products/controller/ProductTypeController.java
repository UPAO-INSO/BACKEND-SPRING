package team.upao.dev.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.controller.BaseController;
import team.upao.dev.common.service.BaseService;
import team.upao.dev.products.dto.ProductTypeRequestDto;
import team.upao.dev.products.dto.ProductTypeResponseDto;
import team.upao.dev.products.service.ProductTypeService;

@RestController
@RequestMapping("/products-types")
@RequiredArgsConstructor
public class ProductTypeController extends BaseController<ProductTypeRequestDto, ProductTypeResponseDto, Long> {

    private final ProductTypeService productTypeService;

    @Override
    protected BaseService<ProductTypeRequestDto, ProductTypeResponseDto, Long> getService() {
        return productTypeService;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductTypeResponseDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productTypeService.findByName(name));
    }
}
