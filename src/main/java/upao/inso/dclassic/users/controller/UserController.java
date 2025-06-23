package upao.inso.dclassic.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.users.model.UserModel;
import upao.inso.dclassic.users.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserModel> create(@RequestBody UserModel user) {
        return ResponseEntity.ok(this.userService.create(user));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<UserModel>> findAll(@ModelAttribute PaginationRequestDto requestDto) {
       return ResponseEntity.ok(this.userService.findAll(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserModel> update(@PathVariable Long id, @RequestBody UserModel user) {
        return ResponseEntity.ok(this.userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        this.userService.delete(id);
    }
}
