package team.upao.dev.integrations.factiliza.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.upao.dev.integrations.factiliza.client.FactilizaClient;
import team.upao.dev.integrations.factiliza.dto.DniResponseDto;
import team.upao.dev.integrations.factiliza.dto.RucResponseDto;
import team.upao.dev.integrations.factiliza.service.FactilizaService;

@Service
@RequiredArgsConstructor
public class FactilizaServiceImpl implements FactilizaService {
    private final FactilizaClient factilizaClient;

    @Override
    public DniResponseDto consultarDni(String dni) {
        return factilizaClient.consultaDni(dni);
    }

    @Override
    public RucResponseDto consultarRuc(String ruc) {
        return factilizaClient.consultaRuc(ruc);
    }
}
