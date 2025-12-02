package team.upao.dev.integrations.factiliza.service;

import team.upao.dev.integrations.factiliza.dto.DniResponseDto;
import team.upao.dev.integrations.factiliza.dto.RucResponseDto;

public interface FactilizaService {
    DniResponseDto consultarDni(String dni);
    RucResponseDto consultarRuc(String ruc);
}
