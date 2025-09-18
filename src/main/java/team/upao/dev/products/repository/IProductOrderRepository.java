package team.upao.dev.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.products.model.ProductOrderModel;

@Repository
public interface IProductOrderRepository extends JpaRepository<ProductOrderModel, Long> {
}
