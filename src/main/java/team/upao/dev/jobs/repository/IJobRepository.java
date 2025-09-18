package team.upao.dev.jobs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.upao.dev.jobs.enums.JobEnum;
import team.upao.dev.jobs.model.JobModel;

import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<JobModel, Long> {
    Optional<JobModel> findByTitle(JobEnum title);
}
