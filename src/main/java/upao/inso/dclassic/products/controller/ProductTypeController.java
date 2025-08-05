package upao.inso.dclassic.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.controller.BaseController;
import upao.inso.dclassic.common.service.BaseService;
import upao.inso.dclassic.products.dto.ProductTypeRequestDto;
import upao.inso.dclassic.products.dto.ProductTypeResponseDto;
import upao.inso.dclassic.products.service.ProductTypeService;

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
