package team.upao.dev.persons.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class PersonResponseDto {
    private Long id;

    private String name;

    private String lastname;

    private String phone;

    private Instant createdAt;

    private Instant updatedAt;
}
