package upao.inso.dclassic.users.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.users.dto.UserDto;
import upao.inso.dclassic.users.model.UserModel;

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

    public UserDto toDto(UserModel userModel) {
        return UserDto.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .email(userModel.getEmail())
                .password(userModel.getPassword())
                .isActive(userModel.getIsActive())
                .role(userModel.getRole())
                .build();
    }

    public List<UserDto> toDto(List<UserModel> userModels) {
        return userModels.stream()
                .map(this::toDto)
                .toList();
    }
}
