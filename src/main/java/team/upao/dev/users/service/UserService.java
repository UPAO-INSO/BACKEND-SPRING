package team.upao.dev.users.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.persons.dto.PersonByFullName;
import team.upao.dev.persons.model.PersonModel;
import team.upao.dev.users.dto.UserDto;
import team.upao.dev.users.dto.UserResponseDto;
import team.upao.dev.users.model.UserModel;

public interface UserService {
    UserResponseDto findById(Long id);
    UserModel findModelById(Long id);
    UserResponseDto findByUsername(String username);
    UserModel findModelByUsername(String username);
    UserResponseDto findByEmail(String email);
    UserResponseDto findByEmailOrUsername(String query);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    PaginationResponseDto<UserResponseDto> findAll(PaginationRequestDto requestDto);
    UserResponseDto create(UserDto user);
    UserModel create(UserModel user);
    UserResponseDto update(Long id, UserDto user);
    UserResponseDto updateUsernameById(Long id, String username);
    UserResponseDto updateEmailById(Long id, String email);
    String delete(Long id);

    PersonByFullName findByUsernameWithFullName(String username);
}
