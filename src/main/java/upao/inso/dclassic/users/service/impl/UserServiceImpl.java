package upao.inso.dclassic.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.exceptions.NotFoundException;
import upao.inso.dclassic.users.dto.UserDto;
import upao.inso.dclassic.users.mapper.UserMapper;
import upao.inso.dclassic.users.model.UserModel;
import upao.inso.dclassic.users.repository.IUserRepository;
import upao.inso.dclassic.users.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findById(Long id){
        UserModel user =  userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        return userMapper.toDto(user);
    }

    @Override
    public UserModel findModelById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDto findByEmailOrUsername(String query) {
        UserModel user =  userRepository
                .findByEmailOrUsername(query, query)
                .orElseThrow(() -> new NotFoundException("User not found with email or username"));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        UserModel user =  userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return userMapper.toDto(user);
    }

    @Override
    public UserModel findModelByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public UserDto findByEmail(String email) {
        UserModel user =  userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        return userMapper.toDto(user);
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
    public PaginationResponseDto<UserDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<UserModel> entities = userRepository.findAll(pageable);
        final List<UserDto> userDtos = userMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                userDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        if (this.existsByUsername(userDto.getUsername())) {
            throw new NotFoundException("Username already exists: " + userDto.getUsername());
        }
        if (this.existsByEmail(userDto.getEmail())) {
            throw new NotFoundException("Email already exists: " + userDto.getEmail());
        }
        UserModel userModel = userMapper.toModel(userDto);
        UserModel saved = userRepository.save(userModel);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserModel create(UserModel userModel) {
        if (this.existsByUsername(userModel.getUsername())) {
            throw new NotFoundException("Username already exists: " + userModel.getUsername());
        }
        if (this.existsByEmail(userModel.getEmail())) {
            throw new NotFoundException("Email already exists: " + userModel.getEmail());
        }
        return userRepository.save(userModel);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto user) {

        UserModel userModel = findModelById(id);

        userModel.setEmail(user.getEmail());
        userModel.setUsername(user.getUsername());
        userModel.setRole(user.getRole());
        userModel.setIsActive(user.getIsActive());
        userModel.setPassword(user.getPassword());

        UserModel saved = userRepository.save(userModel);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserDto updateUsernameById(Long id, String username) {
        UserModel user = findModelById(id);

        if (this.existsByUsername(username)) {
            throw new NotFoundException("Username already exists: " + username);
        }

        userRepository.updateUsernameById(id, username);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateEmailById(Long id, String email) {
        UserModel user = findModelById(id);

        if (this.existsByEmail(email)) {
            throw new NotFoundException("Email already exists: " + email);
        }

        userRepository.updateEmailById(id, email);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        UserModel userModel = findModelById(id);

        userModel.setIsActive(false);
        userRepository.save(userModel);

        return "User with id: " + id + " has been disabled successfully.";
    }
}
