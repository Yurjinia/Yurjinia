package com.yurjinia.common.s3.configuration;

import com.yurjinia.common.environment.EnvironmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Getter
@Configuration
@RequiredArgsConstructor
public class S3Configuration {

    private final EnvironmentService environmentService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.credentials.access-key-id}")
    private String accessKeyId;
    @Value("${aws.credentials.secret-access-key}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        accessKeyId = environmentService.getEnv(accessKeyId);
        secretAccessKey = environmentService.getEnv(secretAccessKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }

}
