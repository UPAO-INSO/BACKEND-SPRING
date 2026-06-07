package team.upao.dev.pensionistas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Builder
public class PensionistaConsumoResponseDto {
    private Long id;
    private LocalDate date;
    private Double priceApplied;
    private Long separacionId;
}
