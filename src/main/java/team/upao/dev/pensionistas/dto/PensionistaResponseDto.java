package team.upao.dev.pensionistas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Builder
public class PensionistaResponseDto {
    private Long id;
    private String name;
    private String phone;
    private Long customerId;
    private String customerName;
    private Integer planCredits;
    private Double planPricePerMeal;
    private Double planTotalPaid;
    private Integer creditsRemaining;
    private Integer creditsUsed;
    private LocalDate startDate;
    private Boolean active;
    private String notes;
}
