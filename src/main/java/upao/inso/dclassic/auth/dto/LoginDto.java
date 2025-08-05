package upao.inso.dclassic.auth.dto;

import lombok.Data;
import upao.inso.dclassic.common.decorators.ValidPassword;

@Data
public class LoginDto {
    private String username;
    @ValidPassword
    private String password;
}
