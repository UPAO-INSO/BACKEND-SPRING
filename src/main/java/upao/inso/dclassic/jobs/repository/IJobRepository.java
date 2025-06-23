package upao.inso.dclassic.jobs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upao.inso.dclassic.jobs.model.JobModel;

import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<JobModel, Long> {
    Optional<JobModel> findByTitle(String title);
}
