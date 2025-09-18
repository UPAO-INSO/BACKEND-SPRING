package team.upao.dev.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.orders.model.OrderEmployeeModel;

@Repository
public interface IOrderEmployeeRepository extends JpaRepository<OrderEmployeeModel, Long> {
}
