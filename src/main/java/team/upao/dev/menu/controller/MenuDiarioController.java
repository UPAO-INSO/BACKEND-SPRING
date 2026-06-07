package team.upao.dev.menu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.menu.dto.MenuDiarioItemRequestDto;
import team.upao.dev.menu.dto.MenuDiarioItemResponseDto;
import team.upao.dev.menu.service.MenuDiarioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("menu-diario")
public class MenuDiarioController {

    private final MenuDiarioService menuDiarioService;

    @PostMapping("/bulk")
    public ResponseEntity<List<MenuDiarioItemResponseDto>> saveForToday(
            @RequestBody @Valid List<MenuDiarioItemRequestDto> items) {
        return ResponseEntity.ok(menuDiarioService.saveForToday(items));
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<MenuDiarioItemResponseDto>> findToday() {
        return ResponseEntity.ok(menuDiarioService.findToday());
    }

    @GetMapping
    public ResponseEntity<List<MenuDiarioItemResponseDto>> findByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(menuDiarioService.findByDate(fecha));
    }

    @PatchMapping("/{id}/porciones")
    public ResponseEntity<MenuDiarioItemResponseDto> updatePortions(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(menuDiarioService.updatePortions(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        return ResponseEntity.ok(menuDiarioService.remove(id));
    }
}
