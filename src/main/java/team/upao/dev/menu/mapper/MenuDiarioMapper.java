package team.upao.dev.menu.mapper;

import org.springframework.stereotype.Component;
import team.upao.dev.menu.dto.MenuDiarioItemResponseDto;
import team.upao.dev.menu.model.MenuDiarioItemModel;

import java.util.List;

@Component
public class MenuDiarioMapper {

    public MenuDiarioItemResponseDto toDto(MenuDiarioItemModel model) {
        return MenuDiarioItemResponseDto.builder()
                .id(model.getId())
                .productId(model.getProduct().getId())
                .productName(model.getProduct().getName())
                .productType(model.getProduct().getProductType() != null
                        ? model.getProduct().getProductType().getName()
                        : null)
                .imageUrl(model.getProduct().getImageUrl())
                .productPrice(model.getProduct().getPrice())
                .date(model.getDate())
                .estimatedPortions(model.getEstimatedPortions())
                .usedPortions(model.getUsedPortions())
                .remainingPortions(model.getRemainingPortions())
                .soldOut(model.isSoldOut())
                .build();
    }

    public List<MenuDiarioItemResponseDto> toDto(List<MenuDiarioItemModel> models) {
        return models.stream().map(this::toDto).toList();
    }
}
