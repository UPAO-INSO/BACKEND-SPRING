package upao.inso.dclassic.tables.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.tables.dto.TableDto;
import upao.inso.dclassic.tables.enums.TableStatus;
import upao.inso.dclassic.tables.model.TableModel;
import upao.inso.dclassic.tables.service.TableService;

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
    public PaginationResponseDto<TableModel> findAllOrders(@ModelAttribute PaginationRequestDto requestDto) {
        return tableService.findAll(requestDto);
    }

    @GetMapping("/filter-by")
    public PaginationResponseDto<TableModel> findAllByStatus(@ModelAttribute PaginationRequestDto requestDto,
                                                             @RequestParam("status") TableStatus status) {
        return tableService.findAllByStatus(requestDto, status);
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
