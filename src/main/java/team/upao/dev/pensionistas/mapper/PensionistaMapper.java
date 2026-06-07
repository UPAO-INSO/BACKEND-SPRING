package team.upao.dev.pensionistas.mapper;

import org.springframework.stereotype.Component;
import team.upao.dev.pensionistas.dto.PensionistaConsumoResponseDto;
import team.upao.dev.pensionistas.dto.PensionistaRequestDto;
import team.upao.dev.pensionistas.dto.PensionistaResponseDto;
import team.upao.dev.pensionistas.model.PensionistaConsumoModel;
import team.upao.dev.pensionistas.model.PensionistaModel;

import java.util.List;

@Component
public class PensionistaMapper {

    public PensionistaModel toModel(PensionistaRequestDto dto) {
        return PensionistaModel.builder()
                .planCredits(dto.getPlanCredits())
                .planPricePerMeal(dto.getPlanPricePerMeal())
                .planTotalPaid(dto.getPlanCredits() * dto.getPlanPricePerMeal())
                .creditsRemaining(dto.getPlanCredits())
                .notes(dto.getNotes())
                .build();
    }

    public PensionistaResponseDto toDto(PensionistaModel model) {
        String firstName = model.getCustomer().getName();
        String lastName  = model.getCustomer().getLastname();
        String fullName  = firstName + (lastName != null && !lastName.isBlank() ? " " + lastName : "");
        return PensionistaResponseDto.builder()
                .id(model.getId())
                .name(fullName)
                .phone(model.getCustomer().getPhone())
                .customerId(model.getCustomer().getId())
                .customerName(fullName)
                .planCredits(model.getPlanCredits())
                .planPricePerMeal(model.getPlanPricePerMeal())
                .planTotalPaid(model.getPlanTotalPaid())
                .creditsRemaining(model.getCreditsRemaining())
                .creditsUsed(model.getPlanCredits() - model.getCreditsRemaining())
                .startDate(model.getStartDate())
                .active(model.getActive())
                .notes(model.getNotes())
                .build();
    }

    public List<PensionistaResponseDto> toDto(List<PensionistaModel> models) {
        return models.stream().map(this::toDto).toList();
    }

    public PensionistaConsumoResponseDto toConsumoDto(PensionistaConsumoModel model) {
        return PensionistaConsumoResponseDto.builder()
                .id(model.getId())
                .date(model.getDate())
                .priceApplied(model.getPriceApplied())
                .separacionId(model.getSeparacion() != null ? model.getSeparacion().getId() : null)
                .build();
    }

    public List<PensionistaConsumoResponseDto> toConsumoDto(List<PensionistaConsumoModel> models) {
        return models.stream().map(this::toConsumoDto).toList();
    }
}
