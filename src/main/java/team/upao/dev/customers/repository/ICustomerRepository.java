package team.upao.dev.customers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.customers.model.CustomerModel;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<CustomerModel, Long> {
    Optional<CustomerModel> findByEmail(String email);
    Optional<CustomerModel> findByPhone(String phone);
    Optional<CustomerModel> findByDocumentNumberContaining(String documentNumber);
    @Query("SELECT c FROM CustomerModel c" +
            " LEFT JOIN PaymentModel p ON c.id = p.customer.id " +
            " WHERE c.id = :customerId")
    Optional<CustomerModel> findByOrderIdInPaymentModel(@Param("customerId") Long customerId);
}
