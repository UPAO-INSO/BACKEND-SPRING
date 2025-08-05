package upao.inso.dclassic.tables.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.tables.dto.TableDto;
import upao.inso.dclassic.tables.model.TableModel;

@Component
@RequiredArgsConstructor
public class TableMapper {
    public TableModel toEntity(TableDto dto) {
        TableModel table = new TableModel();

        table.setNumber(dto.getNumber());
        table.setCapacity(dto.getCapacity());
        table.setFloor(dto.getFloor());

        return table;
    }
}
