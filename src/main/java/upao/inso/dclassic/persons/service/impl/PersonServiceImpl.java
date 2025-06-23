package upao.inso.dclassic.persons.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.persons.model.PersonModel;
import upao.inso.dclassic.persons.repository.IPersonRepository;
import upao.inso.dclassic.persons.service.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final IPersonRepository personRepository;

    @Override
    public PersonModel create(PersonModel personDto) {
        return null;
    }

    @Override
    public PaginationResponseDto<PersonModel> findAll(PaginationRequestDto requestDto) {
        return null;
    }

    @Override
    public PersonModel findById(Long id) {
        return null;
    }

    @Override
    public void update(Long id, PersonModel personDto) {

    }

    @Override
    public void delete(Long id) {

    }
}
