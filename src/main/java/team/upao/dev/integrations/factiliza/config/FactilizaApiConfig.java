package team.upao.dev.integrations.factiliza.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FactilizaApiConfig {

    @Value("${factiliza.api.url}")
    private String baseUrl;

    @Value("${factiliza.api.token}")
    private String apiToken;

    @Bean
    public WebClient factilizaWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiToken)
                .build();
    }
}
