package team.upao.dev.auth.dto;

import lombok.Getter;
import lombok.Setter;
import team.upao.dev.common.decorators.ValidPassword;

@Getter @Setter
public class LoginDto {
    private String username;
    @ValidPassword
    private String password;
}
