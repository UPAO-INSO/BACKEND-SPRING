package upao.inso.dclassic.tables.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.tables.dto.TableDto;
import upao.inso.dclassic.tables.enums.TableStatus;
import upao.inso.dclassic.tables.model.TableModel;

public interface TableService {
    PaginationResponseDto<TableModel> findAll(PaginationRequestDto requestDto);
    PaginationResponseDto<TableModel> findAllByStatus(PaginationRequestDto requestDto, TableStatus status);
    TableModel findById(Long id);
    TableModel findByNumber(String number);
    Boolean existsByNumber(String number);
    TableModel create(TableDto table);
    TableModel update(Long id, TableDto table);
    String delete(Long id);
    TableModel changeStatus(Long id, String status);

}
