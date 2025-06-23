package upao.inso.dclassic.users.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.users.model.UserModel;

public interface UserService {
    UserModel findById(Long id);
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    PaginationResponseDto<UserModel> findAll(PaginationRequestDto requestDto);
    UserModel create(UserModel user);
    UserModel update(Long id, UserModel user);
    void delete(Long id);
}
