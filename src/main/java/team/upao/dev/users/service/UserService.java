package team.upao.dev.users.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.users.dto.UserDto;
import team.upao.dev.users.model.UserModel;

public interface UserService {
    UserDto findById(Long id);
    UserModel findModelById(Long id);
    UserDto findByUsername(String username);
    UserModel findModelByUsername(String username);
    UserDto findByEmail(String email);
    UserDto findByEmailOrUsername(String query);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    PaginationResponseDto<UserDto> findAll(PaginationRequestDto requestDto);
    UserDto create(UserDto user);
    UserModel create(UserModel user);
    UserDto update(Long id, UserDto user);
    UserDto updateUsernameById(Long id, String username);
    UserDto updateEmailById(Long id, String email);
    String delete(Long id);
}
