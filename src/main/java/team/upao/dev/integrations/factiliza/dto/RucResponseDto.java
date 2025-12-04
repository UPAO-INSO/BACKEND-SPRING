package team.upao.dev.integrations.factiliza.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RucResponseDto {
    private int status;
    private boolean success;
    private String message;
    private DataRuc data;

    @Data
    public static class DataRuc {
        private String numero;
        @JsonProperty("nombre_o_razon_social")
        private String nombreORazonSocial;
        @JsonProperty("tipo_contribuyente")
        private String tipoContribuyente;
        private String estado;
        private String condicion;
        private String departamento;
        private String provincia;
        private String distrito;
        private String direccion;
        @JsonProperty("direccion_completa")
        private String direccionCompleta;
        @JsonProperty("ubigeo_sunat")
        private String ubigeoSunat;
        private ArrayList<String> ubigeo;
    }
}
