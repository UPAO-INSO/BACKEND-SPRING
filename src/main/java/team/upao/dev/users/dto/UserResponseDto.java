package team.upao.dev.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.jobs.enums.JobEnum;
import team.upao.dev.users.enums.UserRole;

@Builder
@Getter @Setter
public class UserResponseDto {
    private Long id;

    private String username;

    private String name;

    private String lastname;

    private String email;

    private Boolean isActive;

    private UserRole role;

    private JobEnum jobTitle;
}
