package team.upao.dev.pensionistas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.controller.BaseController;
import team.upao.dev.common.service.BaseService;
import team.upao.dev.pensionistas.dto.PensionistaConsumoResponseDto;
import team.upao.dev.pensionistas.dto.PensionistaRequestDto;
import team.upao.dev.pensionistas.dto.PensionistaResponseDto;
import team.upao.dev.pensionistas.service.PensionistaService;

import java.util.List;

@PreAuthorize("hasAnyRole('GERENTE','ADMINISTRADOR')")
@RestController
@RequiredArgsConstructor
@RequestMapping("pensionistas")
public class PensionistaController extends BaseController<PensionistaRequestDto, PensionistaResponseDto, Long> {

    private final PensionistaService pensionistaService;

    @Override
    protected BaseService<PensionistaRequestDto, PensionistaResponseDto, Long> getService() {
        return pensionistaService;
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PensionistaResponseDto>> findAllActive() {
        return ResponseEntity.ok(pensionistaService.findAllActive());
    }

    @GetMapping("/{id}/consumos")
    public ResponseEntity<List<PensionistaConsumoResponseDto>> findConsumos(@PathVariable Long id) {
        return ResponseEntity.ok(pensionistaService.findConsumos(id));
    }

    @PatchMapping("/{id}/renovar")
    public ResponseEntity<PensionistaResponseDto> renew(
            @PathVariable Long id,
            @RequestParam @Valid Integer creditos) {
        return ResponseEntity.ok(pensionistaService.renew(id, creditos));
    }
}
