package upao.inso.dclassic.vouchers.enums;

import lombok.Getter;

@Getter
public enum VoucherType {
    INVOICE(1),
    RECEIPT(2),
    CREDIT_NOTE(3),
    DEBIT_NOTE(4);

    private final int value;

    VoucherType(int value) {
        this.value = value;
    }
}
