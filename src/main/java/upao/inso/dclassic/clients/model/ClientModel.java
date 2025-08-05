package upao.inso.dclassic.clients.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.persons.model.PersonModel;

import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@Data @AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "clients")
public class ClientModel extends PersonModel {
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String documentNumber;

    @Builder.Default
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderModel> orders = new ArrayList<>();
}
