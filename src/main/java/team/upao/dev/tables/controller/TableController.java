package team.upao.dev.tables.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.tables.dto.TableDto;
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.service.TableService;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public TableModel create(@RequestBody TableDto tableModel) {
        return tableService.create(tableModel);
    }

    @GetMapping
    public PaginationResponseDto<TableModel> findAllOrders(@ModelAttribute PaginationRequestDto requestDto,
                                                           @RequestParam(value = "status", required = false) TableStatus status) {
        return tableService.findAll(requestDto, status);
    }

    @PostMapping("/{id}")
    public TableModel findById(@PathVariable Long id) {
        return tableService.findById(id);
    }

    @PostMapping("/find-by-number/{number}")
    public TableModel findByNumber(@PathVariable String number) {
        return tableService.findByNumber(number);
    }

    @PutMapping("/{id}")
    public TableModel update(@PathVariable Long id, @RequestBody TableDto tableModel) {
        return tableService.update(id, tableModel);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return tableService.delete(id);
    }
}
