package team.upao.dev.auth.dto;

import lombok.Data;
import team.upao.dev.common.decorators.ValidPassword;

@Data
public class LoginDto {
    private String username;
    @ValidPassword
    private String password;
}
