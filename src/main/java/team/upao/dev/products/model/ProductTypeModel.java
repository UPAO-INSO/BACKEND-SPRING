package team.upao.dev.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "product_type")
public class ProductTypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "productType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductModel> products = new ArrayList<>();
}
