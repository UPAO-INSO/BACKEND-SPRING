package team.upao.dev.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.employees.model.EmployeeModel;

import java.util.Optional;

@Repository
public interface IEmployeeRepository extends JpaRepository<EmployeeModel, Long> {
    Optional<EmployeeModel> findFirstByUser_Id(Long userId);
}
