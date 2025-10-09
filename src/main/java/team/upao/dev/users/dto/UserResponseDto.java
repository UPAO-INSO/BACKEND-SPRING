package team.upao.dev.users.dto;

import lombok.Builder;
import lombok.Data;
import team.upao.dev.jobs.enums.JobEnum;
import team.upao.dev.users.enums.UserRole;

@Builder
@Data
public class UserResponseDto {
    private Long id;

    private String username;

    private String fullName;

    private String email;

    private Boolean isActive;

    private UserRole role;

    private JobEnum jobTitle;
}
