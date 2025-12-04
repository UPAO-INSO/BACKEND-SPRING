package team.upao.dev.payments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.payments.enums.PaymentType;
import team.upao.dev.payments.model.PaymentModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPaymentRepository extends JpaRepository<PaymentModel, Long> {
    @Query("SELECT p FROM PaymentModel p WHERE p.order.id = :orderId")
    Optional<PaymentModel> findByOrderId(@Param("orderId") UUID orderId);

    Page<PaymentModel> findAllByState(Pageable pageable, String state);
    Page<PaymentModel> findAllByPaymentType(Pageable pageable, PaymentType paymentType);
    Page<PaymentModel> findAllByStateAndPaymentType(Pageable pageable, String state, PaymentType paymentType);
}
