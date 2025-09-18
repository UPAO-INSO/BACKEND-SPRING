package team.upao.dev.jobs.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.jobs.model.JobModel;

public interface JobService {
    JobModel create(JobModel job);
    PaginationResponseDto<JobModel> findAll(PaginationRequestDto requestDto);
    JobModel findById(Long id);
    JobModel update(Long id, JobModel job);
    void delete(Long id);
    JobModel findByTitle(String title);
}
