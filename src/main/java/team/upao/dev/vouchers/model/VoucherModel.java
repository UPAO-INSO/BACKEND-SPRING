package team.upao.dev.vouchers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.vouchers.enums.CurrencyType;
import team.upao.dev.vouchers.enums.VoucherStatus;
import team.upao.dev.vouchers.enums.VoucherType;

import java.time.Instant;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
//@Entity
//@Table(name = "voucher")
public class VoucherModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String series;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private VoucherType type;

    private Instant issuedAt;

    private CurrencyType currency;

    private Double igvPercentage;

    private Double totalWithIgv;

    private String observations;

    @Column(nullable = false)
    private VoucherStatus status;

    private String pdfUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderModel order;
}
