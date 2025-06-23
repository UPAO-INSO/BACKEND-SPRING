package upao.inso.dclassic.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.users.model.UserModel;
import upao.inso.dclassic.users.repository.IUserRepository;
import upao.inso.dclassic.users.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;

    @Override
    public UserModel findById(Long id){
        Optional<UserModel> user =  userRepository.findById(id);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return user.get();
    }

    @Override
    public UserModel findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    @Override
    public UserModel findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public PaginationResponseDto<UserModel> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<UserModel> entities = userRepository.findAll(pageable);
        final List<UserModel> entitiesDto = entities.stream().toList();
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public UserModel create(UserModel user) {
        return this.userRepository.save(user);
    }

    @Override
    public UserModel update(Long id, UserModel user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
