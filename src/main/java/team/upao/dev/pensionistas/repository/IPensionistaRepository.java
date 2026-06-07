package team.upao.dev.pensionistas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.pensionistas.model.PensionistaModel;

import java.util.List;

@Repository
public interface IPensionistaRepository extends JpaRepository<PensionistaModel, Long> {
    List<PensionistaModel> findAllByActiveTrue();
}
