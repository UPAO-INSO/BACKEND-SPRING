package team.upao.dev.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.common.decorators.ValidPassword;

@Getter @Setter
public class LoginDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;
}
