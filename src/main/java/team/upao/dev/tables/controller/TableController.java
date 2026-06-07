package team.upao.dev.tables.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.tables.dto.TableDto;
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.service.TableService;

@PreAuthorize("hasAnyRole('MESERO','CAJERO','GERENTE','ADMINISTRADOR')")
@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<TableModel> create(@RequestBody @Valid TableDto tableModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.create(tableModel));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<TableModel>> findAll(
            @ModelAttribute @Valid PaginationRequestDto requestDto,
            @RequestParam(value = "status", required = false) TableStatus status) {
        return ResponseEntity.ok(tableService.findAll(requestDto, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.findById(id));
    }

    @GetMapping("/find-by-number/{number}")
    public ResponseEntity<TableModel> findByNumber(@PathVariable String number) {
        return ResponseEntity.ok(tableService.findByNumber(number));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableModel> update(@PathVariable Long id, @RequestBody @Valid TableDto tableModel) {
        return ResponseEntity.ok(tableService.update(id, tableModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.delete(id));
    }
}
