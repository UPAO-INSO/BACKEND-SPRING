package team.upao.dev.persons.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.persons.dto.PersonRequestDto;
import team.upao.dev.persons.dto.PersonResponseDto;
import team.upao.dev.persons.mapper.IPersonMapper;
import team.upao.dev.persons.model.PersonModel;
import team.upao.dev.persons.repository.IPersonRepository;
import team.upao.dev.persons.service.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final IPersonRepository personRepository;
    private final IPersonMapper personMapper;

    @Override
    public PersonResponseDto create(PersonRequestDto personDto) {
        return null;
    }

    @Override
    public PaginationResponseDto<PersonResponseDto> findAll(PaginationRequestDto requestDto) {
        return null;
    }

    @Override
    public PersonResponseDto findById(Long id) {
        PersonModel personModel = this.personRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));

        return personMapper.toDto(personModel);
    }

    @Override
    public PersonResponseDto update(Long id, PersonRequestDto personDto) {
        return null;
    }

    @Override
    public PersonResponseDto partialUpdate(Long aLong, PersonResponseDto dto) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return  null;
    }

    @Override
    public PersonResponseDto findByFullName(String name, String lastname) {
        PersonModel person = personRepository
                .findByNameAndLastnameContaining(name, lastname)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with full name: " + name + " " + lastname));

        return personMapper.toDto(person);
    }
}
