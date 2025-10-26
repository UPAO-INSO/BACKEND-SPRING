package team.upao.dev.orders.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.dto.OrderRequestDto;
import team.upao.dev.orders.dto.OrderResponseDto;
import team.upao.dev.orders.dto.OrderFilterDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.service.OrderService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> create(@RequestBody @Valid OrderRequestDto order) {
        return ResponseEntity.ok(orderService.create(order));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<OrderResponseDto>> findAll(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                           @RequestParam(value = "status", required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.findAll(requestDto, status));
    }

    @PostMapping("/filter-array-status")
    public ResponseEntity<PaginationResponseDto<OrderResponseDto>> findAllByArrayStatus(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                        @RequestBody String[] status) {
        List<OrderStatus> statusList = Arrays.stream(status)
                .map(OrderStatus::valueOf)
                .toList();
        return ResponseEntity.ok(orderService.findAllByArrayStatus(requestDto, statusList));
    }

    @PostMapping("/filter-by-tables-and-status")
    public ResponseEntity<PaginationResponseDto<OrderResponseDto>> findAllByTablesAndStatus(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                            @RequestBody @Valid OrderFilterDto orderFilterDto) {
        return ResponseEntity.ok(orderService
                .findAllByTablesAndStatus(requestDto, orderFilterDto.getTableIds(), orderFilterDto.getOrderStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/by-tableId/{tableId}")
    public ResponseEntity<PaginationResponseDto<OrderResponseDto>> findByTableId(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                                 @PathVariable Long tableId) {
        return ResponseEntity.ok(orderService.findAllByTableId(requestDto, tableId));
    }

    @PostMapping("/tables")
    public ResponseEntity<List<OrderResponseDto>> findByTableIds(@RequestBody Long[] tableIds) {
        List<Long> ids = List.of(tableIds);
        return ResponseEntity.ok(orderService.findByTableIds(ids));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> update(@PathVariable Long id, @RequestBody @Valid OrderRequestDto order) {
        return ResponseEntity.ok(orderService.update(id, order));
    }

    @PatchMapping("/status")
    public ResponseEntity<OrderResponseDto> changeStatus(@RequestBody @Valid ChangeOrderStatusDto order) {
        return ResponseEntity.ok(orderService.changeStatus(order));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.delete(id));
    }
}
