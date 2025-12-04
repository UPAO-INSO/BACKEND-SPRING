package team.upao.dev.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.inventory.dto.InventoryRequestDto;
import team.upao.dev.inventory.dto.InventoryResponseDto;
import team.upao.dev.inventory.dto.InventoryFilterDto;
import team.upao.dev.inventory.dto.InventoryUpdateDto;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.service.InventoryService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;

    /**
     * HU1 + HU2: Registrar insumo o producto en inventario
     * POST /inventory
     * Cuerpo: InventoryRequestDto con nombre, cantidad, tipo, unidad de medida
     */
    @PostMapping
    public ResponseEntity<InventoryResponseDto> create(@Valid @RequestBody InventoryRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.create(request));
    }

    /**
     * HU4: Obtener un ítem específico del inventario
     * GET /inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    /**
     * HU4: Listar todos los ítems del inventario (con filtro opcional por tipo)
     * GET /inventory?type=INGREDIENT
     * GET /inventory?type=BEVERAGE
     * GET /inventory (sin filtro = todos)
     */
    @GetMapping
    public ResponseEntity<PaginationResponseDto<InventoryResponseDto>> findAll( @ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                @RequestParam(value = "type", required = false) InventoryType type) {
        return ResponseEntity.ok(inventoryService.findAll(requestDto, type));
    }

    /**
     * HU4: Búsqueda por nombre
     * GET /inventory/search?term=aceite
     */
    @GetMapping("/search")
    public ResponseEntity<PaginationResponseDto<InventoryResponseDto>> searchByName( @ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                     @RequestParam String term) {
        return ResponseEntity.ok(inventoryService.searchByName(requestDto, term));
    }

    /**
     * HU4: Filtrar por múltiples tipos
     * POST /inventory/filter-types
     * Cuerpo: ["INGREDIENT", "BEVERAGE"]
     */
    @PostMapping("/filter-types")
    public ResponseEntity<PaginationResponseDto<InventoryResponseDto>> findAllByTypes(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                      @RequestBody List<InventoryType> types) {
        return ResponseEntity.ok(inventoryService.findAllByArrayTypes(requestDto, types));
    }

    /**
     * HU4: Filtro avanzado por nombre Y tipo
     * POST /inventory/filter
     * Cuerpo: InventoryFilterDto { type: "INGREDIENT", searchTerm: "aceite" }
     */
    @PostMapping("/filter")
    public ResponseEntity<PaginationResponseDto<InventoryResponseDto>> filterByNameAndType(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                            @Valid @RequestBody InventoryFilterDto filter) {
        return ResponseEntity.ok(inventoryService.findAllByNameAndType(requestDto, filter));
    }

    /**
     * HU3: Modificar un ítem del inventario
     * PUT /inventory/{id}
     * Cuerpo: InventoryUpdateDto (parcial, solo los campos que quieras actualizar)
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> update(@PathVariable Long id, @Valid @RequestBody InventoryUpdateDto request) {
        return ResponseEntity.ok(inventoryService.update(id, request));
    }

    /**
     * Eliminar un ítem del inventario
     * DELETE /inventory/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.ok("Item eliminado exitosamente");
    }
}