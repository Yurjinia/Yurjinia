package com.yurjinia.jobmanager.job.repository;


import com.yurjinia.jobmanager.job.entity.JobEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<JobEntity, String> {

    Optional<JobEntity> findByJobName(String jobId);

}
