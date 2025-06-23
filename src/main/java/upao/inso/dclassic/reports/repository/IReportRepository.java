package upao.inso.dclassic.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.reports.model.ReportModel;

@Repository
public interface IReportRepository extends JpaRepository<ReportModel, Long> {
}
