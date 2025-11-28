package team.upao.dev.integrations.culqi.config;

import com.culqi.Culqi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "culqi")
public class CulqiConfig {

    @Value("${app.culqi.public-key}")
    private String publicKey;

    @Value("${app.culqi.secret-key}")
    private String secretKey;

    @Bean
    @SuppressWarnings("static-access")
    public Culqi init() {
        Culqi culqi = new Culqi();
        culqi.public_key = publicKey;
        culqi.secret_key = secretKey;
        return culqi;
    }

}
