package upao.inso.dclassic.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.products.model.ProductOrderModel;

@Repository
public interface IProductOrderRepository extends JpaRepository<ProductOrderModel, Long> {
}
