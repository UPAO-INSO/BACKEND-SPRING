package team.upao.dev.separaciones.mapper;

import org.springframework.stereotype.Component;
import team.upao.dev.separaciones.dto.SeparacionItemResponseDto;
import team.upao.dev.separaciones.dto.SeparacionResponseDto;
import team.upao.dev.separaciones.model.SeparacionItemModel;
import team.upao.dev.separaciones.model.SeparacionModel;

import java.util.List;

@Component
public class SeparacionMapper {

    public SeparacionResponseDto toDto(SeparacionModel model) {
        return SeparacionResponseDto.builder()
                .id(model.getId())
                .date(model.getDate())
                .pensionistaId(model.getPensionista() != null ? model.getPensionista().getId() : null)
                .pensionistaName(model.getPensionista() != null ? model.getPensionista().getCustomer().getName() : null)
                .customerId(model.getCustomer() != null ? model.getCustomer().getId() : null)
                .clientName(resolveClientName(model))
                .status(model.getStatus())
                .totalPrice(model.getTotalPrice())
                .notes(model.getNotes())
                .createdAt(model.getCreatedAt())
                .items(model.getItems().stream().map(this::toItemDto).toList())
                .build();
    }

    public List<SeparacionResponseDto> toDto(List<SeparacionModel> models) {
        return models.stream().map(this::toDto).toList();
    }

    public SeparacionItemResponseDto toItemDto(SeparacionItemModel item) {
        return SeparacionItemResponseDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productType(item.getProduct().getProductType() != null
                        ? item.getProduct().getProductType().getName()
                        : null)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build();
    }

    private String resolveClientName(SeparacionModel model) {
        if (model.getPensionista() != null) return model.getPensionista().getCustomer().getName();
        if (model.getClientName() != null) return model.getClientName();
        if (model.getCustomer() != null) {
            String ln = model.getCustomer().getLastname();
            return model.getCustomer().getName() + (ln != null && !ln.isBlank() ? " " + ln : "");
        }
        return "Sin nombre";
    }
}
