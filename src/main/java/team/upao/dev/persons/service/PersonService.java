package team.upao.dev.persons.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.persons.model.PersonModel;

public interface PersonService {
    PersonModel create(PersonModel personDto);
    PaginationResponseDto<PersonModel> findAll(PaginationRequestDto requestDto);
    PersonModel findById(Long id);
    void update(Long id, PersonModel personDto);
    void delete(Long id);
}
