package upao.inso.dclassic.common.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.service.BaseService;

public abstract class BaseController<T, K, ID> {
    protected abstract BaseService<T, K, ID> getService();

    @PostMapping
    public ResponseEntity<K> create(@RequestBody @Valid T dto) {
        return ResponseEntity.ok(getService().create(dto));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<K>> findAll(@ModelAttribute @Valid PaginationRequestDto requestDto) {
        return ResponseEntity.ok(getService().findAll(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<K> findById(@PathVariable ID id) {
        return ResponseEntity.ok(getService().findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<K> update(@PathVariable ID id, @RequestBody @Valid T dto) {
        return ResponseEntity.ok(getService().update(id, dto));
    }

    @PatchMapping("/partial/{id}")
    public ResponseEntity<K> partialUpdate(@PathVariable ID id, @RequestBody @Valid K type) {
        return ResponseEntity.ok(getService().partialUpdate(id, type));
    }

    @PatchMapping

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable ID id) {
        return ResponseEntity.ok(getService().delete(id));
    }
}
