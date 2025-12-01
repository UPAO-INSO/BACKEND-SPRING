package team.upao.dev.inventory.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.inventory.dto.InventoryRequestDto;
import team.upao.dev.inventory.dto.InventoryResponseDto;
import team.upao.dev.inventory.model.InventoryModel;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryMapper {
    
    
    public InventoryModel toModel(InventoryRequestDto dto) {
        return InventoryModel.builder()
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .type(dto.getType())
                .unitOfMeasure(dto.getUnitOfMeasure())
                .build();
    }
    
   
    public InventoryResponseDto toDto(InventoryModel model) {
        return InventoryResponseDto.builder()
                .id(model.getId())
                .name(model.getName())
                .quantity(model.getQuantity())
                .type(model.getType())
                .unitOfMeasure(model.getUnitOfMeasure())
                .build();
    }
    
  
    public List<InventoryResponseDto> toDto(List<InventoryModel> items) {
        return items.stream()
                .map(this::toDto)
                .toList();
    }
}

