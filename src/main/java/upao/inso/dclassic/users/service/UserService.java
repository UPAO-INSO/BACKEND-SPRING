package upao.inso.dclassic.users.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.users.dto.UserDto;
import upao.inso.dclassic.users.model.UserModel;

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
