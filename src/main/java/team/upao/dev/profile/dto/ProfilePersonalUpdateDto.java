package team.upao.dev.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilePersonalUpdateDto {
    @NotBlank(message = "Name is required")
    private String name;

    private String lastname;

    private String phone;
}
