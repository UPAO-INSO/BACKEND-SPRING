package upao.inso.dclassic.jobs.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.jobs.model.JobModel;

public interface JobService {
    JobModel create(JobModel job);
    PaginationResponseDto<JobModel> findAll(PaginationRequestDto requestDto);
    JobModel findById(Long id);
    JobModel update(Long id, JobModel job);
    void delete(Long id);
    JobModel findByTitle(String title);
}
