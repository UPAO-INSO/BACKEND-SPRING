package team.upao.dev.profile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.profile.dto.ProfilePasswordUpdateDto;
import team.upao.dev.profile.dto.ProfilePersonalUpdateDto;
import team.upao.dev.profile.dto.ProfileResponseDto;
import team.upao.dev.profile.service.ProfileService;

@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PatchMapping("/personal")
    public ResponseEntity<ProfileResponseDto> updatePersonal(@Valid @RequestBody ProfilePersonalUpdateDto dto) {
        return ResponseEntity.ok(profileService.updatePersonal(dto));
    }

    @PatchMapping("/username")
    public ResponseEntity<ProfileResponseDto> updateUsername(@RequestParam String username) {
        return ResponseEntity.ok(profileService.updateUsername(username));
    }

    @PatchMapping("/email")
    public ResponseEntity<ProfileResponseDto> updateEmail(@RequestParam String email) {
        return ResponseEntity.ok(profileService.updateEmail(email));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ProfilePasswordUpdateDto dto) {
        profileService.updatePassword(dto);
        return ResponseEntity.noContent().build();
    }
}
