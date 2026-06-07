package team.upao.dev.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    private String role;
    private String jobTitle;
    private Instant lastLogin;
    private Boolean isActive;
}
