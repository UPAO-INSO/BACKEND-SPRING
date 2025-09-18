package team.upao.dev.vouchers.enums;

import lombok.Getter;

@Getter
public enum SunatTransactionType {
    VENTA_INTERNA(1),
    EXPORTACION(2),
    VENTA_INTERNA_ANTICIPOS(4),
    VENTAS_NO_DOMICILIADOS(29), // No qualify for export
    OPERACION_SUJETA_DETRACCION(30),
    DETRACCION_TRANSPORTE_CARGA(33),
    OPERACION_SUJETA_PERCEPCION(34),
    DETRACCION_TRANSPORTE_PASAJEROS(32),
    DETRACCION_RECURSOS_HIDROBIOLOGICOS(31),
    VENTA_NACIONAL_TURISTAS_TAX_FREE(35);

    private final int value;

    SunatTransactionType(int value) {
        this.value = value;
    }
}
