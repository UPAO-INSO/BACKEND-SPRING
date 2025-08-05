package upao.inso.dclassic.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.users.dto.FindUserDto;
import upao.inso.dclassic.users.dto.UserDto;
import upao.inso.dclassic.users.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto user) {
        return ResponseEntity.ok(this.userService.create(user));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<UserDto>> findAll(@ModelAttribute @Valid PaginationRequestDto requestDto) {
       return ResponseEntity.ok(this.userService.findAll(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }

    @GetMapping("/find")
    public ResponseEntity<UserDto> findByEmailOrUsername(@ModelAttribute @Valid FindUserDto findUserDto) {
        return ResponseEntity.ok(this.userService.findByEmailOrUsername(findUserDto.query()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Valid UserDto user) {
        return ResponseEntity.ok(this.userService.update(id, user));
    }

    @PatchMapping("/username/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUsernameById(@PathVariable Long id, @RequestParam String username) {
        return ResponseEntity.ok(this.userService.updateUsernameById(id, username));
    }

    @PatchMapping("email/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateEmailById(@PathVariable Long id, @RequestParam String email) {
        return ResponseEntity.ok(this.userService.updateEmailById(id, email));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
