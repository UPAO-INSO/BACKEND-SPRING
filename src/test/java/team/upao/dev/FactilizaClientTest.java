package team.upao.dev;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import team.upao.dev.integrations.factiliza.client.FactilizaClient;
import team.upao.dev.integrations.factiliza.dto.DniResponseDto;
import team.upao.dev.integrations.factiliza.dto.RucResponseDto;

import static org.junit.jupiter.api.Assertions.*;

class FactilizaClientTest {

    @Test
    void consultaRuc_shouldReturnParsedDto() throws Exception {
        MockWebServer server = new MockWebServer();
        try {
            String json = """
                {
                  "status":200,
                  "success":true,
                  "message":"ok",
                  "data":{
                    "numero":"20123456789",
                    "nombre_o_razon_social":"ACME S.A.C.",
                    "estado":"ACTIVO",
                    "condicion":"HABIDO",
                    "direccion":"Av. Ejemplo 123"
                  }
                }
                """;

            server.enqueue(new MockResponse()
                    .setBody(json)
                    .setHeader("Content-Type", "application/json"));

            server.start();

            WebClient webClient = WebClient.builder()
                    .baseUrl(server.url("/").toString())
                    .build();

            FactilizaClient client = new FactilizaClient(webClient);
            RucResponseDto dto = client.consultaRuc("20123456789");

            assertNotNull(dto);
            assertEquals("20123456789", dto.getData().getNumero());
            assertEquals("ACME S.A.C.", dto.getData().getNombreORazonSocial());
            assertEquals("ACTIVO", dto.getData().getEstado());
            assertEquals("HABIDO", dto.getData().getCondicion());
            assertEquals("Av. Ejemplo 123", dto.getData().getDireccion());
        } finally {
            server.shutdown();
        }
    }

    @Test
    void consultaDni_shouldReturnParsedDto() throws Exception {
        MockWebServer server = new MockWebServer();
        try {
            String json = """
                {
                  "status":200,
                  "success":true,
                  "message":"ok",
                  "data":{
                    "numero":"12345678",
                    "nombres":"Juan",
                    "apellido_paterno":"Perez",
                    "apellido_materno":"Lopez",
                    "estado":"HALLADO"
                  }
                }
                """;

            server.enqueue(new MockResponse()
                    .setBody(json)
                    .setHeader("Content-Type", "application/json"));

            server.start();

            WebClient webClient = WebClient.builder()
                    .baseUrl(server.url("/").toString())
                    .build();

            FactilizaClient client = new FactilizaClient(webClient);
            DniResponseDto dto = client.consultaDni("12345678");

            assertNotNull(dto);
            assertEquals("12345678", dto.getData().getNumero());
            assertEquals("Juan", dto.getData().getNombres());
            assertEquals("Perez", dto.getData().getApellidoPaterno());
            assertEquals("Lopez", dto.getData().getApellidoMaterno());
        } finally {
            server.shutdown();
        }
    }
}
