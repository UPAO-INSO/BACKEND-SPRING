package upao.inso.dclassic.jobs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.jobs.enums.JobEnum;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.jobs.repository.IJobRepository;
import upao.inso.dclassic.jobs.service.JobService;
import upao.inso.dclassic.users.model.UserModel;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<JobModel> entities = jobRepository.findAll(pageable);
        final List<JobModel> entitiesDto = entities.stream().toList();
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public JobModel findById(Long id) {
        Optional<JobModel> job = this.jobRepository.findById(id);

        if(job.isEmpty()) {
            throw new IllegalArgumentException("Job not found with id: " + id);
        }

        return job.get();
    }

    @Override
    public JobModel update(Long id, JobModel job) {
        Optional<JobModel> existingJob = this.jobRepository.findById(id);

        if(existingJob.isEmpty()) {
            throw new IllegalArgumentException("Job not found with id: " + id);
        }

        JobModel updatedJob = existingJob.get();
        updatedJob.setTitle(job.getTitle());
        updatedJob.setEmployees(job.getEmployees());

        return this.jobRepository.save(updatedJob);
    }

    @Override
    public void delete(Long id) {
        this.findById(id);
    }

    @Override
    public JobModel findByTitle(String title) {

        JobEnum jobEnum = JobEnum.valueOf(title);
        Optional<JobModel> job = this.jobRepository.findByTitle(jobEnum);

        if(job.isEmpty()) {
            throw new IllegalArgumentException("Job not found with title: " + title);
        }

        return job.get();
    }
}
