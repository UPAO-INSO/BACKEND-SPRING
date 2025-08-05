package upao.inso.dclassic.tables.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.tables.model.TableModel;

import java.util.Optional;

@Repository
public interface ITableRepository extends JpaRepository<TableModel, Long> {
    Optional<TableModel> findByNumber(String number);
    Boolean existsByNumber(String number);
}
