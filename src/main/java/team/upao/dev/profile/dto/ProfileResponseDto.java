package team.upao.dev.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.jobs.enums.JobEnum;
import team.upao.dev.users.enums.UserRole;

import java.time.Instant;

@Builder
@Getter
@Setter
public class ProfileResponseDto {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private UserRole role;
    private JobEnum jobTitle;
    private Instant lastLogin;
    private Boolean isActive;
}
