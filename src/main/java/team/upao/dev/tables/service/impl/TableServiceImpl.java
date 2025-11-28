package team.upao.dev.tables.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.tables.dto.TableDto;
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.mapper.TableMapper;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.repository.ITableRepository;
import team.upao.dev.tables.service.TableService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableServiceImpl implements TableService {
    private final ITableRepository tableRepository;
    private final TableMapper tableMapper;

    @Override
    public PaginationResponseDto<TableModel> findAll(PaginationRequestDto requestDto, TableStatus status) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<TableModel> entities;

        if (status != null) {
            entities = tableRepository.findAllByStatus(pageable, status);
        } else {
            entities = tableRepository.findAll(pageable);
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    }

    @Override
    public TableModel findByNumber(String number) {
        return tableRepository
                .findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + number));
    }

    @Override
    public Boolean existsByNumber(String number) {
        return tableRepository.existsByNumber(number);
    }

    @Override
    public TableModel create(TableDto table) {
        if(existsByNumber(table.getNumber())) {
            throw new ResourceNotFoundException("Table already exists with number: " + table.getNumber());
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
    public TableModel changeStatus(Long id, TableStatus status) {
        TableModel table = findById(id);

        table.setStatus(status);
        tableRepository.save(table);

        return table;
    }
}
