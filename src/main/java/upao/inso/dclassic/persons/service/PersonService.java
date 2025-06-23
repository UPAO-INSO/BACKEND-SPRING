package upao.inso.dclassic.persons.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.persons.model.PersonModel;

public interface PersonService {
    PersonModel create(PersonModel personDto);
    PaginationResponseDto<PersonModel> findAll(PaginationRequestDto requestDto);
    PersonModel findById(Long id);
    void update(Long id, PersonModel personDto);
    void delete(Long id);
}
