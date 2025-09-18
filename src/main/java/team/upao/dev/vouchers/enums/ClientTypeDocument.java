package team.upao.dev.vouchers.enums;

import lombok.Getter;

@Getter
public enum ClientTypeDocument {
    RUC("6"),
    DNI("1"),
    VARIOS("-"), // Sales smaller than S/ 700.00
    CARNET_EXTRANJERIA("4"),
    PASAPORTE("7"),
    CEDULA_DIPLOMATICA("A"),
    DOC_IDENT_PAIS_RESIDENCIA("B"),
    NO_DOMICILIADO("0"),
    SALVOCONDUCTO("G");

    private final String code;

    ClientTypeDocument(String code) {
        this.code = code;
    }

}

