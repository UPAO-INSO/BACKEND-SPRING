package team.upao.dev.persons.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter @Setter
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "persons",
        indexes = {
                @Index(name = "idx_persons_phone", columnList = "phone"),
                @Index(name = "idx_persons_name", columnList = "name"),
                @Index(name = "idx_persons_lastname", columnList = "lastname")
        }
)
public class PersonModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    @CreatedDate
    private Instant updatedAt;
}
