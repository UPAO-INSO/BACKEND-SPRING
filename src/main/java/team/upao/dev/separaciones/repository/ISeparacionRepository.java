package team.upao.dev.separaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.separaciones.enums.SeparacionStatus;
import team.upao.dev.separaciones.model.SeparacionModel;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ISeparacionRepository extends JpaRepository<SeparacionModel, Long> {
    List<SeparacionModel> findAllByDateOrderByCreatedAtAsc(LocalDate date);
    List<SeparacionModel> findAllByDateAndStatusOrderByCreatedAtAsc(LocalDate date, SeparacionStatus status);
}
