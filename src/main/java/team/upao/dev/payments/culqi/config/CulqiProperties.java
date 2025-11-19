package team.upao.dev.payments.culqi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "culqi")
public class CulqiProperties {
    @NotBlank
    private String baseUrl;

    @NotBlank
    private String secureBaseUrl;

    @NotBlank
    private String secretKey;

    @NotBlank
    private String publicKey;

    private String webhookSignature;

    private String yapeReturnUrl;

    @NotBlank
    private String defaultCurrency = "PEN";

    @NotNull
    private Integer timeoutConnectSeconds = 5;

    @NotNull
    private Integer timeoutReadSeconds = 15;
}
