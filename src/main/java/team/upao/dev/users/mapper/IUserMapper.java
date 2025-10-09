package team.upao.dev.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import team.upao.dev.jobs.enums.JobEnum;
import team.upao.dev.users.dto.UserDto;
import team.upao.dev.users.dto.UserResponseDto;
import team.upao.dev.users.model.UserModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserResponseDto toDto(UserModel user);
    List<UserResponseDto> toDto(List<UserModel> users);

    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "jobTitle", source = "jobTitle")
    UserResponseDto toDtoWithFullNameAndJobTitle(UserModel user, String fullName, JobEnum jobTitle);

    UserModel toModel(UserDto dto);
}
