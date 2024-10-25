package com.yurjinia.jobmanager.job.service;

import com.yurjinia.jobmanager.job.entity.JobEntity;
import com.yurjinia.jobmanager.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public void save(JobEntity jobEntity) {
        jobRepository.save(jobEntity);
    }

    public Optional<JobEntity> findByJobName(String name) {
        return jobRepository.findByJobName(name);
    }

}
