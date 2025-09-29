package team.upao.dev.orders.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.dto.OrderDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.service.OrderService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderDto order) {
        return ResponseEntity.ok(orderService.create(order));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<OrderDto>> findAll(@ModelAttribute @Valid PaginationRequestDto requestDto,
                                                                  @RequestParam(value = "status", required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.findAll(requestDto, status));
    }

    @PostMapping("/filter-array-status")
    public ResponseEntity<PaginationResponseDto<OrderDto>> findAllByArrayStatus(@ModelAttribute @Valid  PaginationRequestDto requestDto,
                                                                                 @RequestBody String[] status) {
        List<OrderStatus> statusList = Arrays.stream(status)
                .map(OrderStatus::valueOf)
                .toList();
        return ResponseEntity.ok(orderService.findAllByArrayStatus(requestDto, statusList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping("/tables")
    public ResponseEntity<List<OrderDto>> findByTableId(@RequestBody Long[] tableIds) {
        List<Long> ids = List.of(tableIds);
        return ResponseEntity.ok(orderService.findByTableIds(ids));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody @Valid OrderDto order) {
        return ResponseEntity.ok(orderService.update(id, order));
    }

    @PatchMapping("/status")
    public ResponseEntity<OrderDto> changeStatus(@RequestBody @Valid ChangeOrderStatusDto order) {
        return ResponseEntity.ok(orderService.changeStatus(order));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.delete(id));
    }
}
