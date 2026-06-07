package team.upao.dev.customers.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.customers.enums.DocumentType;
import team.upao.dev.persons.model.PersonModel;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_customers_document_number", columnList = "document_number", unique = true),
                @Index(name = "idx_customers_email", columnList = "email", unique = true)
        }
)
public class CustomerModel extends PersonModel {
    @Column(nullable = true, length = 200)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DocumentType documentType;

    @Column(nullable = true, length = 20)
    private String documentNumber;

    @Column(nullable = true)
    private String departament;

    @Column(nullable = true)
    private String province;

    @Column(nullable = true)
    private String district;

    @Column(nullable = true)
    private String completeAddress;
}
