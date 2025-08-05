package upao.inso.dclassic.orders.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.orders.dto.ChangeOrderStatusDto;
import upao.inso.dclassic.orders.dto.OrderDto;
import upao.inso.dclassic.orders.service.OrderService;

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
    public ResponseEntity<PaginationResponseDto<OrderDto>> findAll(@ModelAttribute PaginationRequestDto requestDto) {
        return ResponseEntity.ok(orderService.findAll(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
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
