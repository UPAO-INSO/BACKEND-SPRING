package team.upao.dev.pensionistas.service;

import team.upao.dev.common.service.BaseService;
import team.upao.dev.pensionistas.dto.PensionistaConsumoResponseDto;
import team.upao.dev.pensionistas.dto.PensionistaRequestDto;
import team.upao.dev.pensionistas.dto.PensionistaResponseDto;
import team.upao.dev.pensionistas.model.PensionistaModel;
import team.upao.dev.separaciones.model.SeparacionModel;

import java.util.List;

public interface PensionistaService extends BaseService<PensionistaRequestDto, PensionistaResponseDto, Long> {
    List<PensionistaResponseDto> findAllActive();
    List<PensionistaConsumoResponseDto> findConsumos(Long pensionistaId);
    PensionistaResponseDto renew(Long id, Integer additionalCredits);
    PensionistaModel findModelById(Long id);

    /** Descuenta un crédito y registra el consumo. Llamado desde SeparacionService al entregar. */
    void registrarConsumo(PensionistaModel pensionista, SeparacionModel separacion);
}
