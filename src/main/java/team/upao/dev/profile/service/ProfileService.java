package team.upao.dev.profile.service;

import team.upao.dev.profile.dto.ProfilePasswordUpdateDto;
import team.upao.dev.profile.dto.ProfilePersonalUpdateDto;
import team.upao.dev.profile.dto.ProfileResponseDto;

public interface ProfileService {
    ProfileResponseDto getProfile();
    ProfileResponseDto updatePersonal(ProfilePersonalUpdateDto dto);
    ProfileResponseDto updateUsername(String username);
    ProfileResponseDto updateEmail(String email);
    void updatePassword(ProfilePasswordUpdateDto dto);
}
