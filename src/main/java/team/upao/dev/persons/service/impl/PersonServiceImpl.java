package team.upao.dev.persons.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.persons.model.PersonModel;
import team.upao.dev.persons.repository.IPersonRepository;
import team.upao.dev.persons.service.PersonService;

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
