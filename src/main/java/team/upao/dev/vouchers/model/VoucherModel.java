package team.upao.dev.vouchers.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import team.upao.dev.payments.model.PaymentModel;
import team.upao.dev.vouchers.enums.CurrencyType;
import team.upao.dev.vouchers.enums.VoucherType;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vouchers")
public class VoucherModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String series;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant issuedAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CurrencyType currency = CurrencyType.PEN;

    @Builder.Default
    private Double igvPercentage = 18.0;

    @Column(nullable = false)
    private Double totalGravada;

    @Column(nullable = false)
    private Double totalIgv;

    @Column(nullable = false)
    private Double total;

    private String observations;

    @Column(nullable = false)
    private String pdfUrl;

    @Column(nullable = false)
    private String xmlUrl;

    @Column(nullable = false)
    private String qrCodeString;

    @Column(nullable = false)
    private String barCode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private PaymentModel paymentModel;
}
