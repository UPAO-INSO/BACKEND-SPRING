package team.upao.dev.vouchers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.vouchers.model.VoucherModel;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherModel, Long> {
}
