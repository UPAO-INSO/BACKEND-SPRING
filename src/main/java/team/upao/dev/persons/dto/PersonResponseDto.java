package team.upao.dev.persons.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PersonResponseDto {
    private Long id;

    private String name;

    private String lastname;

    private String phone;

    private Instant createdAt;

    private Instant updatedAt;
}
