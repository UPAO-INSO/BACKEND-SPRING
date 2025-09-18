package team.upao.dev.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String TOKEN_APP = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(TOKEN_APP))
                .components(new Components().addSecuritySchemes(TOKEN_APP, createAPIKeyScheme()))
                .info(new Info()
                        .title("D'Classic Restaurant API")
                        .description("API para gestionar pedidos y ventas del restaurante D'Classic.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("D'Classic Team")
                                .email("d.classic@email.com")
                                .url("https://dclassic.com"))
                        .license(
                                new License()
                                .name("Licencia MIT")
                                .url("https://opensource.org/licenses/MIT"))
                );

    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
