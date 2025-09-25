package team.upao.dev.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import team.upao.dev.users.dto.UserDto;
import team.upao.dev.users.dto.UserResponseDto;
import team.upao.dev.users.model.UserModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserResponseDto toDto(UserModel user);
    List<UserResponseDto> toDto(List<UserModel> users);

    @Mapping(target = "fullName", source = "fullName")
    UserResponseDto toDtoWithFullName(UserModel user, String fullName);

    UserModel toModel(UserDto dto);
}
