package upao.inso.dclassic.auth.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upao.inso.dclassic.common.decorators.ValidPassword;
import upao.inso.dclassic.jobs.enums.JobEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastname;

    private String phone;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword()
    private String password;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Job title is required")
    private String jobTitle;
}
