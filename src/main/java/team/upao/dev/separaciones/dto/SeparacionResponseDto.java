package team.upao.dev.separaciones.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.separaciones.enums.SeparacionStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Builder
public class SeparacionResponseDto {
    private Long id;
    private LocalDate date;
    private Long pensionistaId;
    private String pensionistaName;
    private Long customerId;
    private String clientName;
    private SeparacionStatus status;
    private Double totalPrice;
    private String notes;
    private Instant createdAt;
    private List<SeparacionItemResponseDto> items;
}
