package team.upao.dev.profile.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.employees.model.EmployeeModel;
import team.upao.dev.employees.repository.IEmployeeRepository;
import team.upao.dev.exceptions.DuplicateResourceException;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.profile.dto.ProfilePasswordUpdateDto;
import team.upao.dev.profile.dto.ProfilePersonalUpdateDto;
import team.upao.dev.profile.dto.ProfileResponseDto;
import team.upao.dev.profile.service.ProfileService;
import team.upao.dev.users.model.UserModel;
import team.upao.dev.users.repository.IUserRepository;
import team.upao.dev.users.enums.UserRole;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final IUserRepository userRepository;
    private final IEmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private UserModel currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ProfileResponseDto toDto(UserModel user, EmployeeModel employee) {
        UserRole role = user.getRole();
        return ProfileResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(role != null ? role.name() : null)
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .name(employee != null ? employee.getName() : null)
                .lastname(employee != null ? employee.getLastname() : null)
                .phone(employee != null ? employee.getPhone() : null)
                .jobTitle(employee != null && employee.getJob() != null
                        ? employee.getJob().getTitle().name() : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile() {
        UserModel user = currentUser();
        EmployeeModel employee = employeeRepository.findFirstByUser_Id(user.getId()).orElse(null);
        return toDto(user, employee);
    }

    @Override
    @Transactional
    public ProfileResponseDto updatePersonal(ProfilePersonalUpdateDto dto) {
        UserModel user = currentUser();
        EmployeeModel employee = employeeRepository.findFirstByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found"));

        employee.setName(dto.getName());
        employee.setLastname(dto.getLastname());
        employee.setPhone(dto.getPhone());
        employeeRepository.save(employee);

        return toDto(user, employee);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateUsername(String username) {
        UserModel user = currentUser();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Username already exists: " + username);
        }

        userRepository.updateUsernameById(user.getId(), username);
        user.setUsername(username);

        EmployeeModel employee = employeeRepository.findFirstByUser_Id(user.getId()).orElse(null);
        return toDto(user, employee);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateEmail(String email) {
        UserModel user = currentUser();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }

        userRepository.updateEmailById(user.getId(), email);
        user.setEmail(email);

        EmployeeModel employee = employeeRepository.findFirstByUser_Id(user.getId()).orElse(null);
        return toDto(user, employee);
    }

    @Override
    @Transactional
    public void updatePassword(ProfilePasswordUpdateDto dto) {
        UserModel user = currentUser();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
