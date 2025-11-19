package team.upao.dev.payments.culqi.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CulqiYapeTokenRequest {
    private String otp;

    @JsonProperty("number_phone")
    private String phoneNumber;

    /**
     * Amount must be expressed in cents and as a string as required by Culqi.
     */
    private String amount;

    private Map<String, String> metadata;
}
