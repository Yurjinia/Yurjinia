package com.yurjinia.jobmanager.job.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobEntity {
    private String jobName;
    private boolean isDone;
}
