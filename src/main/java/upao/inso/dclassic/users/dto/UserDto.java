package upao.inso.dclassic.users.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import upao.inso.dclassic.users.enums.UserRole;

public class UserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
