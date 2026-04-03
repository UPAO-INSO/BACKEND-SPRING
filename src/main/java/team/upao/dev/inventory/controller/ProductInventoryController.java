package team.upao.dev.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.inventory.dto.ProductInventoryRequestDto;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.dto.ProductInventoryUpdateDto;
import team.upao.dev.inventory.service.ProductInventoryService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-inventory")
public class ProductInventoryController {
    
    private final ProductInventoryService productInventoryService;

    /**
     * HU1: Registrar la relación o receta de un producto (asociar producto con inventario)
     * POST /product-inventory
     * Cuerpo: ProductInventoryRequestDto { productId, inventoryId, quantity, unitOfMeasure }
     * Ejemplo: Pizza requiere 0.5 kg de harina
     */
    @PostMapping
    public ResponseEntity<ProductInventoryResponseDto> create( @Valid @RequestBody ProductInventoryRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productInventoryService.createProductInventory(request));
    }

    /**
     * HU4: Visualizar la receta completa de un producto
     * GET /product-inventory/recipe/{productId}
     * Retorna: Lista de ingredientes/bebidas del producto con cantidades
     */
    @GetMapping("/recipe/{productId}")
    public ResponseEntity<List<ProductInventoryResponseDto>> getRecipe(@PathVariable Long productId) {
        return ResponseEntity.ok(productInventoryService.getRecipeByProductId(productId));
    }

    /**
     * HU4: Obtener detalle de una relación específica
     * GET /product-inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductInventoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productInventoryService.getProductInventoryById(id));
    }

    /**
     * HU4: Listar todas las relaciones producto-inventario
     * GET /product-inventory
     */
    @GetMapping
    public ResponseEntity<List<ProductInventoryResponseDto>> getAll() {
        return ResponseEntity.ok(productInventoryService.getAllProductInventories());
    }

    /**
     * HU3: Modificar la receta de un producto
     * PUT /product-inventory/{id}
     * Cuerpo: ProductInventoryRequestDto con nuevos valores
     * Ejemplo: Cambiar de 0.5 kg a 0.6 kg de harina en la pizza
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductInventoryResponseDto> update(@PathVariable Long id,
                                                                @Valid @RequestBody ProductInventoryUpdateDto request) {
        return ResponseEntity.ok(productInventoryService.updateProductInventory(id, request));
    }

    /**
     * Eliminar una relación producto-inventario
     * DELETE /product-inventory/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productInventoryService.deleteProductInventory(id);
        return ResponseEntity.ok("Relación eliminada exitosamente");
    }
}