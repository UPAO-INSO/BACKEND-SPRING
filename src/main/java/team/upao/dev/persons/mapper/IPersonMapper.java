package team.upao.dev.persons.mapper;

import org.mapstruct.Mapper;
import team.upao.dev.persons.dto.PersonResponseDto;
import team.upao.dev.persons.model.PersonModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPersonMapper {
    PersonResponseDto toDto(PersonModel person);
    List<PersonResponseDto> toDto(List<PersonModel> persons);

    PersonModel toModel(PersonResponseDto dto);
    List<PersonModel> toModel(List<PersonResponseDto> dtos);
}
