package team.upao.dev.tables.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.tables.dto.TableDto;
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.model.TableModel;

public interface TableService {
    PaginationResponseDto<TableModel> findAll(PaginationRequestDto requestDto, TableStatus status);
    TableModel findById(Long id);
    TableModel findByNumber(String number);
    Boolean existsByNumber(String number);
    TableModel create(TableDto table);
    TableModel update(Long id, TableDto table);
    String delete(Long id);
    TableModel changeStatus(Long id, TableStatus status);
}
