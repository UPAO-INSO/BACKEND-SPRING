package upao.inso.dclassic.jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.jobs.enums.JobEnum;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.jobs.repository.IJobRepository;
import upao.inso.dclassic.jobs.service.JobService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final IJobRepository jobRepository;

    public Double getSalaryByJobTitle(JobModel job) {
        return switch (job.getTitle()) {
            case GERENTE -> 5000.0;
            case CAJERO -> 1500.0;
            case COCINERO -> 1600.0;
            case MESERO -> 600.0;
        };
    }

    @Override
    public JobModel create(JobModel job) {
        return this.jobRepository.save(job);
    }

    @Override
    public PaginationResponseDto<JobModel> findAll(PaginationRequestDto requestDto) {
        return null;
    }

    @Override
    public JobModel findById(Long id) {
        return null;
    }

    @Override
    public JobModel update(Long id, JobModel job) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public JobModel findByTitle(String title) {
        Optional<JobModel> job = this.jobRepository.findByTitle(title);

        if(job.isEmpty()) {
            throw new IllegalArgumentException("Job not found with title: " + title);
        }

        return job.get();
    }
}
