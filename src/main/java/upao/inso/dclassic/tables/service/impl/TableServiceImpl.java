package upao.inso.dclassic.tables.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.exceptions.NotFoundException;
import upao.inso.dclassic.tables.dto.TableDto;
import upao.inso.dclassic.tables.enums.TableStatus;
import upao.inso.dclassic.tables.mapper.TableMapper;
import upao.inso.dclassic.tables.model.TableModel;
import upao.inso.dclassic.tables.repository.ITableRepository;
import upao.inso.dclassic.tables.service.TableService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableServiceImpl implements TableService {

    private final ITableRepository tableRepository;
    private final TableMapper tableMapper;

    @Override
    public PaginationResponseDto<TableModel> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<TableModel> entities = tableRepository.findAll(pageable);
        final List<TableModel> entitiesDto = entities.stream().toList();
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public PaginationResponseDto<TableModel> findAllByStatus(PaginationRequestDto requestDto, TableStatus status) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<TableModel> entities = tableRepository.findAllByStatus(pageable, status);
        final List<TableModel> entitiesDto = entities.stream().toList();
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public TableModel findById(Long id) {
        return tableRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Table not found with id: " + id));
    }

    @Override
    public TableModel findByNumber(String number) {
        return tableRepository
                .findByNumber(number)
                .orElseThrow(() -> new NotFoundException("Table not found with id: " + number));
    }

    @Override
    public Boolean existsByNumber(String number) {
        return tableRepository.existsByNumber(number);
    }

    @Override
    public TableModel create(TableDto table) {
        if(existsByNumber(table.getNumber())) {
            throw new NotFoundException("Table already exists with number: " + table.getNumber());
        }

        log.info("Table create: {}", table);

        TableModel tableModel = tableMapper.toEntity(table);

        return tableRepository.save(tableModel);
    }

    @Override
    public TableModel update(Long id, TableDto table) {
        TableModel tableExisting = findById(id);

        tableExisting.setNumber(table.getNumber());
        tableExisting.setStatus(table.getStatus());
        tableExisting.setCapacity(table.getCapacity());

        return tableRepository.save(tableExisting);
    }

    @Override
    public String delete(Long id) {
        return "";
    }

    @Override
    public TableModel changeStatus(Long id, String status) {
        return null;
    }
}
