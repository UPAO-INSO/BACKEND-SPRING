package upao.inso.dclassic.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.employees.model.EmployeeModel;

@Repository
public interface IEmployeeRepository extends JpaRepository<EmployeeModel, Long> {
}
