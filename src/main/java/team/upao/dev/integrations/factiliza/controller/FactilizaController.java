package team.upao.dev.integrations.factiliza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.upao.dev.integrations.factiliza.dto.DniResponseDto;
import team.upao.dev.integrations.factiliza.dto.RucResponseDto;
import team.upao.dev.integrations.factiliza.service.FactilizaService;

@RestController
@RequestMapping("factiliza")
@RequiredArgsConstructor
public class FactilizaController {
    private final FactilizaService factilizaService;

    @GetMapping("/consultar/dni/{dni}")
    public ResponseEntity<DniResponseDto> consultarDni(@PathVariable String dni) {
        return ResponseEntity.ok(factilizaService.consultarDni(dni));
    }

    @GetMapping("/consultar/ruc/{ruc}")
    public ResponseEntity<RucResponseDto> consultarRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(factilizaService.consultarRuc(ruc));
    }
}
