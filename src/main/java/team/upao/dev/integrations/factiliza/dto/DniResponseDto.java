package team.upao.dev.integrations.factiliza.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DniResponseDto {
    private int status;
    private boolean success;
    private String message;
    private DataDni data;

    @Data
    public static class DataDni {
        private String numero;
        private String nombres;
        @JsonProperty("apellido_paterno")
        private String apellidoPaterno;
        @JsonProperty("apellido_materno")
        private String apellidoMaterno;
        @JsonProperty("nombre_completo")
        private String nombreCompleto;
        private String departamento;
        private String provincia;
        private String distrito;
        private String direccion;
        private String direccion_completa;
        @JsonProperty("ubigeo_reniec")
        private String ubigeoReniec;
        @JsonProperty("ubigeo_sunat")
        private String ubigeoSunat;
        private ArrayList<String> ubigeo;
        @JsonProperty("fecha_nacimiento")
        private String fechaNacimiento;
        private String sexo;
    }
}
