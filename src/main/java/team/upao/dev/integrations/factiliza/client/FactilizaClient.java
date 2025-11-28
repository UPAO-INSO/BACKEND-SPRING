package team.upao.dev.integrations.factiliza.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import team.upao.dev.integrations.factiliza.dto.DniResponseDto;
import team.upao.dev.integrations.factiliza.dto.RucResponseDto;
import team.upao.dev.integrations.factiliza.exception.FactilizaClientException;

@Slf4j
@Service
public class FactilizaClient {

    private final WebClient webClient;

    public FactilizaClient(@Qualifier("factilizaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public RucResponseDto consultaRuc(String ruc) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ruc/info")
                        .pathSegment(ruc)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class).map(FactilizaClientException::new))
                .bodyToMono(RucResponseDto.class)
                .block();
    }

    public DniResponseDto consultaDni(String dni) {
        log.info("consulta dni {}", dni);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/dni/info")
                        .pathSegment(dni)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class).map(FactilizaClientException::new))
                .bodyToMono(DniResponseDto.class)
                .block();
    }
}
