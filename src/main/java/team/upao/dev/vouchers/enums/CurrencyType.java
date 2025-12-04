package team.upao.dev.vouchers.enums;

import lombok.Getter;

@Getter
public enum CurrencyType {
    PEN(1),
    USD(2);

    private final int code;

    CurrencyType(int code) {
        this.code = code;
    }

    public CurrencyType getByCode(int code) {
        for (CurrencyType currencyType : CurrencyType.values()) {
            if (currencyType.getCode() == code) {
                return currencyType;
            }
        }
        return null;
    }
}
