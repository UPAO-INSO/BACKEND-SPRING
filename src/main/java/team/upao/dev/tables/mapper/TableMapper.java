package team.upao.dev.tables.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.tables.dto.TableDto;
import team.upao.dev.tables.model.TableModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TableMapper {

    public TableDto toDto(TableModel entity) {
        return TableDto.builder()
                .number(entity.getNumber())
                .capacity(entity.getCapacity())
                .floor(entity.getFloor())
                .status(entity.getStatus())
                .isActive(entity.getIsActive())
                .build();
    }

    public List<TableDto> toDto(List<TableModel> entityList) {
        return entityList.stream().map(this::toDto).toList();
    }

    public TableModel toEntity(TableDto dto) {
        TableModel table = new TableModel();

        table.setNumber(dto.getNumber());
        table.setCapacity(dto.getCapacity());
        table.setFloor(dto.getFloor());

        return table;
    }

    public List<TableModel> toEntity(List<TableDto> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }
}
