package com.yurjinia.common.s3.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

    private final Tika tika;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    // ToDo: make this method work asynchronously, probably we need to use Spring WebFlux
    public String uploadFile(MultipartFile file, String key) {
        try {
            String mimeType = tika.detect(file.getBytes());

            if (!mimeType.contains(IMAGE_JPEG_VALUE) &&
                    !mimeType.contains(IMAGE_PNG_VALUE)) {
                throw new CommonException(ErrorCode.FILE_TYPE_ERROR, HttpStatus.BAD_REQUEST);
            }

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            //ToDo: check uploadFile method for case - when we get URL we can probably receive null, and we need to handle it.
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
        } catch (IOException e) {
            throw new CommonException(ErrorCode.UPLOAD_FILE_ERROR, HttpStatus.EXPECTATION_FAILED);
        }
    }

    public Optional<String> uploadImage(MultipartFile image, String key) {
        if (image == null || image.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(uploadFile(image, key));
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            String imageKey = uri.getPath().substring(1);

            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageKey)
                    .build());
        } catch (URISyntaxException | S3Exception e) {
            throw new CommonException(ErrorCode.DELETE_FILE_ERROR, HttpStatus.EXPECTATION_FAILED);
        }
    }

}
