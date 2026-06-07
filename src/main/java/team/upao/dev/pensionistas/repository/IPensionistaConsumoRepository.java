package team.upao.dev.pensionistas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.pensionistas.model.PensionistaConsumoModel;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPensionistaConsumoRepository extends JpaRepository<PensionistaConsumoModel, Long> {
    List<PensionistaConsumoModel> findAllByPensionistaIdOrderByDateDesc(Long pensionistaId);
    boolean existsByPensionistaIdAndDate(Long pensionistaId, LocalDate date);
}
