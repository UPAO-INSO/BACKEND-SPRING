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

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "jobTitle", source = "jobTitle")
    UserResponseDto toDtoWithFullNameAndJobTitle(UserModel user, String name, String lastname, JobEnum jobTitle);

    UserModel toModel(UserDto dto);
}
