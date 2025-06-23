package upao.inso.dclassic.vouchers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.vouchers.enums.ClientTypeDocument;
import upao.inso.dclassic.vouchers.enums.CurrencyType;
import upao.inso.dclassic.vouchers.enums.VoucherStatus;
import upao.inso.dclassic.vouchers.enums.VoucherType;

import java.time.Instant;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "voucher")
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

    private ClientTypeDocument clientTypeDocument;

    private String clientDocumentNumber;

    private String clientDenomination;

    private String clientAddress;

    private String clientEmail;

    private Instant issuedAt;

    private CurrencyType currency;

    private Double igvPercentage;

    private Double totalWithoutIgv;

    private Double totalIgv;

    private Double totalWithIgv;

    private String observations;

    private Boolean sentToSunat;

    private Boolean sentToClient;

    @Column(nullable = false)
    private VoucherStatus status;

    private String pdfUrl;

    private String xmlUrl;

    private String cdrUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = true)
    private OrderModel order;
}
