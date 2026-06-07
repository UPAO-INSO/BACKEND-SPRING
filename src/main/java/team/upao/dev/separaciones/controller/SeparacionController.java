package team.upao.dev.separaciones.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.separaciones.dto.SeparacionRequestDto;
import team.upao.dev.separaciones.dto.SeparacionResponseDto;
import team.upao.dev.separaciones.dto.SeparacionStatusUpdateDto;
import team.upao.dev.separaciones.service.SeparacionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("separaciones")
public class SeparacionController {

    private final SeparacionService separacionService;

    @PostMapping
    public ResponseEntity<SeparacionResponseDto> create(@RequestBody @Valid SeparacionRequestDto dto) {
        return ResponseEntity.ok(separacionService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeparacionResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(separacionService.findById(id));
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<SeparacionResponseDto>> findToday() {
        return ResponseEntity.ok(separacionService.findToday());
    }

    @GetMapping("/por-fecha")
    public ResponseEntity<List<SeparacionResponseDto>> findByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(separacionService.findByDate(fecha));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SeparacionResponseDto> changeStatus(
            @PathVariable Long id,
            @RequestBody @Valid SeparacionStatusUpdateDto dto) {
        return ResponseEntity.ok(separacionService.changeStatus(id, dto));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(separacionService.cancel(id));
    }
}
