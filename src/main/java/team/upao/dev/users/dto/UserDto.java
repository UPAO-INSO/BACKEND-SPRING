package team.upao.dev.users.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.users.enums.UserRole;

@Builder
@Getter @Setter
public class UserDto {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    private String username;

    @NotBlank(message = "Username is required")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    @NotNull
    private Boolean isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
