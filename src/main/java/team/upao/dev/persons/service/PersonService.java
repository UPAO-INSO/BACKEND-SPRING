package team.upao.dev.persons.service;

import team.upao.dev.common.service.BaseService;
import team.upao.dev.persons.dto.PersonRequestDto;
import team.upao.dev.persons.dto.PersonResponseDto;

public interface PersonService extends BaseService<PersonRequestDto, PersonResponseDto, Long> {
    PersonResponseDto findByFullName(String name, String lastname);
}
