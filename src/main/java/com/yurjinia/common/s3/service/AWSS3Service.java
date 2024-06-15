package com.yurjinia.common.s3.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;

import static com.yurjinia.common.s3.constants.AWSS3Constants.FORWARD_SLASH;
import static com.yurjinia.common.s3.constants.AWSS3Constants.MAIN_PACKAGE;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

    private final Tika tika;
    private final S3Client s3Client;
    private final JwtService jwtService;
    private final UserRepository userRepository;

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

    public void changeAvatar(MultipartFile image) {
        String currentUserEmail = jwtService.getCurrentUserToken().getUserName();
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(currentUserEmail);

        if (userEntityOpt.isEmpty()) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userEntityOpt.get();
        String key = MAIN_PACKAGE + userEntity.getEmail() + FORWARD_SLASH + image.getOriginalFilename();
        String fileURL = uploadFile(image, key);

        userEntity.setAvatarId(fileURL);

        userRepository.save(userEntity);
    }

    public Optional<String> uploadAvatar(UserEntity userEntity, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return Optional.empty();
        } else {
            String key = MAIN_PACKAGE + userEntity.getEmail() + FORWARD_SLASH + image.getOriginalFilename();

            return Optional.of(uploadFile(image, key));
        }
    }

    public void deleteAvatar() {
        String currentUserEmail = jwtService.getCurrentUserToken().getUserName();
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(currentUserEmail);

        if (userEntityOpt.isEmpty()) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userEntityOpt.get();

        userEntity.setAvatarId(null);

        userRepository.save(userEntity);
    }

}
