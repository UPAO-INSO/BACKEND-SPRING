package team.upao.dev.vouchers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.upao.dev.vouchers.model.VoucherModel;

import java.util.Optional;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherModel, Long> {

    /**
     * Retorna el mayor número de comprobante (parseado como entero) para una
     * serie dada. Si no hay comprobantes en esa serie, retorna empty.
     */
    @Query("SELECT MAX(CAST(v.number AS integer)) FROM VoucherModel v WHERE v.series = :series")
    Optional<Integer> findMaxNumberBySeries(@Param("series") String series);
}
