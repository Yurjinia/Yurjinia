package com.yurjinia.jobmanager.job.service;


import com.yurjinia.jobmanager.job.entity.JobEntity;
import com.yurjinia.jobmanager.job.enums.JobStatus;
import com.yurjinia.jobmanager.kafka.KafkaBrokerService;
import com.yurjinia.jobmanager.kafka.constant.KafkaConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FillTestDataJob {

    private final JobService jobService;
    private final KafkaBrokerService kafkaBrokerService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FillTestDataJob.class);

    @PostConstruct
    public void init() {
        if (!isJobDone(FillTestDataJob.class.getSimpleName())) {
            kafkaBrokerService.send(KafkaConstants.TOPIC, JobStatus.INIT);
        }
    }

    @KafkaListener(topics = "yurjinia", groupId = "jobs")
    private void listenYurjinia(JobStatus status) {
        if (status.equals(JobStatus.DONE)) {
            jobService.save(JobEntity.builder().jobName(FillTestDataJob.class.getSimpleName()).isDone(true).build());
        }
    }

    private boolean isJobDone(String jobName) {
        return jobService.findByJobName(jobName)
                .filter(JobEntity::isDone)
                .map(job -> true)
                .orElseGet(() -> {
                    log.error("Job not found or not done");
                    return false;
                });
    }

}
