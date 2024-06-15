package com.yurjinia.user.controller;

import com.yurjinia.common.s3.service.AWSS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final AWSS3Service awsS3Service;

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void changeAvatar(@RequestPart(value = "image") MultipartFile image) {
        awsS3Service.changeAvatar(image);
    }

    @DeleteMapping("/avatar")
    public void deleteAvatar() {
        awsS3Service.deleteAvatar();
    }

}
