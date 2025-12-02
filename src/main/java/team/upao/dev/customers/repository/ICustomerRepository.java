package team.upao.dev.customers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.customers.model.CustomerModel;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<CustomerModel, Long> {
    Optional<CustomerModel> findByEmail(String email);
    Optional<CustomerModel> findByPhone(String phone);
    Optional<CustomerModel> findByDocumentNumberContaining(String documentNumber);
}
