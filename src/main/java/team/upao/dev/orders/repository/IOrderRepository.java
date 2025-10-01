package team.upao.dev.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.model.OrderModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<OrderModel, Long> {
    Page<OrderModel> findAllByOrderStatus(Pageable pageable, OrderStatus status);
    Page<OrderModel> findAllByOrderStatusIn(Pageable pageable, List<OrderStatus> orderStatuses);
    Page<OrderModel> findAllByTableIdInAndOrderStatusIn(Pageable pageable, List<Long> tableIds, List<OrderStatus> orderStatuses);
    Optional<List<OrderModel>> findByTableIdIn(List<Long> tableIds);
}
