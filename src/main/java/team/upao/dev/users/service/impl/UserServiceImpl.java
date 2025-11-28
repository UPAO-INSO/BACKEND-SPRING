package team.upao.dev.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.persons.dto.PersonByFullName;
import team.upao.dev.users.dto.UserDto;
import team.upao.dev.users.dto.UserResponseDto;
import team.upao.dev.users.mapper.IUserMapper;
import team.upao.dev.users.model.UserModel;
import team.upao.dev.users.repository.IUserRepository;
import team.upao.dev.users.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    private PersonByFullName parseCommaSeparatedFullName(String fullName) {
        if (fullName == null) return new PersonByFullName(null, null);

        String[] parts = fullName.split(",", 2);
        String name = parts[0].trim();
        String lastname = parts.length > 1 ? parts[1].trim() : null;

        if (name.isEmpty()) name = null;
        if (lastname != null && lastname.isEmpty()) lastname = null;

        return new PersonByFullName(name, lastname);
    }

    @Override
    public PersonByFullName findByUsernameWithFullName(String username) {
        String names = userRepository.findUserWithFullNameByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with username: " + username));

        return parseCommaSeparatedFullName(names);
    }

    @Override
    public UserResponseDto findById(Long id){
        UserModel user =  userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return userMapper.toDto(user);
    }

    @Override
    public UserModel findModelById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserResponseDto findByEmailOrUsername(String query) {
        UserModel user =  userRepository
                .findByEmailOrUsername(query, query)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email or username"));

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto findByUsername(String username) {
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
    public UserResponseDto findByEmail(String email) {
        UserModel user =  userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

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
    public PaginationResponseDto<UserResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<UserModel> entities = userRepository.findAll(pageable);
        List<UserResponseDto> userDtos = userMapper.toDto(entities.getContent());
        userDtos.forEach(u -> {
            PersonByFullName person = this.findByUsernameWithFullName(u.getUsername());
            u.setName(person.name());
            u.setLastname(person.lastname());
        });
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
    public UserResponseDto create(UserDto userDto) {
        if (this.existsByUsername(userDto.getUsername())) {
            throw new ResourceNotFoundException("Username already exists: " + userDto.getUsername());
        }
        if (this.existsByEmail(userDto.getEmail())) {
            throw new ResourceNotFoundException("Email already exists: " + userDto.getEmail());
        }
        UserModel userModel = userMapper.toModel(userDto);
        UserModel saved = userRepository.save(userModel);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserModel create(UserModel userModel) {
        if (this.existsByUsername(userModel.getUsername())) {
            throw new ResourceNotFoundException("Username already exists: " + userModel.getUsername());
        }
        if (this.existsByEmail(userModel.getEmail())) {
            throw new ResourceNotFoundException("Email already exists: " + userModel.getEmail());
        }
        return userRepository.save(userModel);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserDto user) {

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
    public UserResponseDto updateUsernameById(Long id, String username) {
        UserModel user = findModelById(id);

        if (this.existsByUsername(username)) {
            throw new ResourceNotFoundException("Username already exists: " + username);
        }

        userRepository.updateUsernameById(id, username);

        UserResponseDto userResponseDto = userMapper.toDto(user);
        return userResponseDto;
    }

    @Override
    @Transactional
    public UserResponseDto updateEmailById(Long id, String email) {
        UserModel user = findModelById(id);

        if (this.existsByEmail(email)) {
            throw new ResourceNotFoundException("Email already exists: " + email);
        }

        userRepository.updateEmailById(id, email);

        UserResponseDto userResponseDto = userMapper.toDto(user);
        return userResponseDto;
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
