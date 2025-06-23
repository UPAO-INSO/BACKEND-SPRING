package upao.inso.dclassic.orders.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.orders.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{employeeId}")
    public ResponseEntity<OrderModel> create(@PathVariable Long employeeId, @RequestBody OrderModel order) {
        return ResponseEntity.ok(orderService.create(employeeId, order));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<OrderModel>> findAll(@ModelAttribute PaginationRequestDto requestDto) {
        return ResponseEntity.ok(orderService.findAll(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderModel> update(@PathVariable Long id, @RequestBody OrderModel user) {
        return ResponseEntity.ok(orderService.update(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
