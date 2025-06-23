package upao.inso.dclassic.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;

@Repository
public interface IOrderEmployeeRepository extends JpaRepository<OrderEmployeeModel, Long> {
}
