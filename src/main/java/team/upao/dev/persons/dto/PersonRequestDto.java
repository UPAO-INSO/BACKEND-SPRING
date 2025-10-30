package team.upao.dev.persons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter @Setter
@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
public class PersonRequestDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Lastname is required")
    private String lastname;

    @NotBlank(message = "Phone is required")
    @Size(min =  12, max = 12)
    private String phone;

    private Instant createdAt;

    private Instant updatedAt;
}
