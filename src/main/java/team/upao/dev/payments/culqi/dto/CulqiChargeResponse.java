package team.upao.dev.payments.culqi.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CulqiChargeResponse {
    private String object;
    private String id;

    @JsonProperty("creation_date")
    private Long creationDate;

    private Integer amount;

    @JsonProperty("currency_code")
    private String currencyCode;

    private String email;

    private Outcome outcome;

    @JsonProperty("reference_code")
    private String referenceCode;

    @JsonProperty("authorization_code")
    private String authorizationCode;

    private Map<String, Object> metadata;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Outcome {
        private String type;
        private String code;

        @JsonProperty("user_message")
        private String userMessage;

        @JsonProperty("merchant_message")
        private String merchantMessage;
    }
}
