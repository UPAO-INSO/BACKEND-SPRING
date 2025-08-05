package upao.inso.dclassic.persons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
public class PersonDto {
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
