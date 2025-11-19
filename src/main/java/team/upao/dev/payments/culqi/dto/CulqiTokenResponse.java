package team.upao.dev.payments.culqi.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CulqiTokenResponse {
    private String object;
    private String id;
    private String type;

    @JsonProperty("creation_date")
    private Long creationDate;

    private String email;

    @JsonProperty("last_four")
    private String lastFour;

    private Boolean active;

    private Map<String, Object> metadata;
}
