package team.upao.dev.users.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.users.dto.UserDto;
import team.upao.dev.users.dto.UserResponseDto;
import team.upao.dev.users.model.UserModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserModel toModel(UserDto userDto) {
        return UserModel.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .isActive(userDto.getIsActive())
                .role(userDto.getRole())
                .build();
    }

    public UserResponseDto toDto(UserModel userModel) {
        return UserResponseDto.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .email(userModel.getEmail())
                .isActive(userModel.getIsActive())
                .role(userModel.getRole())
                .build();
    }

    public List<UserResponseDto> toDto(List<UserModel> userModels) {
        return userModels.stream()
                .map(this::toDto)
                .toList();
    }
}
