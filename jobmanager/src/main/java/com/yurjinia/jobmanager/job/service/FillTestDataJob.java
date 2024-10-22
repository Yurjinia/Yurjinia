package com.yurjinia.jobmanager.job.service;


import com.yurjinia.jobmanager.job.entity.JobEntity;
import com.yurjinia.jobmanager.kafka.KafkaBrokerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FillTestDataJob {

    private final JobService jobService;
    private final KafkaBrokerService kafkaBrokerService;

    @PostConstruct
    public void init() {
        if (!isJobDone(FillTestDataJob.class.getSimpleName())) {
            kafkaBrokerService.send("yurjinia", "init");
        }
    }

    @KafkaListener(topics = "yurjinia", groupId = "jobs")
    private void listenYurjinia(String status) {
        if (status.equals("Done")) {
            jobService.save(JobEntity.builder().jobName(FillTestDataJob.class.getSimpleName()).isDone(true).build());
        }
    }

    private boolean isJobDone(String jobName) {
        Optional<JobEntity> job = jobService.findByJobName(jobName);
        return job.map(JobEntity::isDone).orElse(false);
    }

}
