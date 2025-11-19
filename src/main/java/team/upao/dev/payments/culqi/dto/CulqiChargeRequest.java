package team.upao.dev.payments.culqi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CulqiChargeRequest {
    private String amount;

    @JsonProperty("currency_code")
    private String currencyCode;

    private String email;

    @JsonProperty("source_id")
    private String sourceId;

    private Boolean capture;
    private String description;
    private Map<String, String> metadata;

    @JsonProperty("antifraud_details")
    private AntifraudDetails antifraudDetails;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AntifraudDetails {
        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        private String address;

        @JsonProperty("address_city")
        private String addressCity;

        @JsonProperty("country_code")
        private String countryCode;

        @JsonProperty("phone_number")
        private String phoneNumber;
    }
}
