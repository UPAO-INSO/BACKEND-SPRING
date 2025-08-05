package upao.inso.dclassic.jobs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.jobs.service.JobService;

@RestController
@RequestMapping("jobs")
@Slf4j
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobModel> create(@RequestBody JobModel job) {
        return ResponseEntity.ok(this.jobService.create(job));
    }

    @GetMapping
    public PaginationResponseDto<JobModel> findAll(PaginationRequestDto requestDto) {
        return this.jobService.findAll(requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.jobService.findById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobModel> update(@PathVariable Long id, @RequestBody JobModel job) {
        return ResponseEntity.ok(this.jobService.update(id, job));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        this.jobService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
