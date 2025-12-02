package team.upao.dev.integrations.culqi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ClientDetailsRequestDTO {
    @JsonProperty("first_name")
    @Size(min = 2, max = 50)
    @NotBlank
    private String firstName;

    @JsonProperty("last_name")
    @Size(min = 2, max = 50)
    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @JsonProperty("phone_number")
    @Size(min = 5, max = 15)
    @NotBlank
    private String phoneNumber;
}
