package team.upao.dev.payments.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.payments.enums.PaymentType;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_order_number", columnList = "order_number"),
        @Index(name = "idx_payments_external_id", columnList = "external_id")
})
public class PaymentModel {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String provider; // "culqi"

    @Column(length = 100, nullable = false)
    private String externalId; // culqi id

    @Column(precision = 19, scale = 2, nullable = false)
    private Integer amount;

    @Column(length = 10, nullable = false)
    private String currencyCode;

    @Column(length = 500, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerModel customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(length = 50, nullable = false)
    private String state;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalFee;

    @Column(precision = 19, scale = 2)
    private BigDecimal netAmount;

    @Column(length = 1000, nullable = false)
    private String qr;

    @Column(length = 1000, nullable = false)
    private String urlPe;

    // Timestamps relevantes: convertir epoch -> Instant antes de persistir
    @Column(nullable = false)
    private Instant creationDate;

    @Column(nullable = false)
    private Instant expirationDate;

    @Column(nullable = false)
    private Instant updatedAt;

    @Column
    private Instant paidAt;

    @Lob
    @Column(name = "raw_response", nullable = false, columnDefinition = "LONGTEXT")
    private String rawResponse;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column
    private Instant modifiedAt;
}
