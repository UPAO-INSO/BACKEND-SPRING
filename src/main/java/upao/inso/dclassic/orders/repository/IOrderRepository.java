package upao.inso.dclassic.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.orders.enums.OrderStatus;
import upao.inso.dclassic.orders.model.OrderModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<OrderModel, Long> {
    Optional<List<OrderModel>> findAllByOrderStatus(OrderStatus orderStatus);
}
