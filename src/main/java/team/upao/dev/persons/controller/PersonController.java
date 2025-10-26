package team.upao.dev.persons.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.controller.BaseController;
import team.upao.dev.common.service.BaseService;
import team.upao.dev.persons.dto.PersonByFullName;
import team.upao.dev.persons.dto.PersonRequestDto;
import team.upao.dev.persons.dto.PersonResponseDto;
import team.upao.dev.persons.service.PersonService;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonController extends BaseController<PersonRequestDto, PersonResponseDto, Long> {
    private final PersonService personService;

    @Override
    protected BaseService<PersonRequestDto, PersonResponseDto, Long> getService() {
        return personService;
    }

    @PostMapping("/by-full-name")
    public PersonResponseDto findByFullName(@RequestBody @Valid PersonByFullName person) {
        return personService.findByFullName(person.name(), person.lastname());
    }
}
