package team.upao.dev.vouchers.enums;

import lombok.Getter;

@Getter
public enum CurrencyType {
    SOLES(1),
    DOLARES(2),
    EUROS(3),
    LIBRA_ESTERLINA(4);

    private final int code;

    CurrencyType(int code) {
        this.code = code;
    }
}
