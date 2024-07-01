package com.yurjinia.user.controller;

import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.dto.UsernameDTO;
import com.yurjinia.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/{userEmail}/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserProfileDTO getUserProfile(@PathVariable String userEmail) {
        return userProfileService.getUserProfile(userEmail);
    }

    @PutMapping("/username")
    public UserProfileDTO changeUsername(@PathVariable String userEmail, @Valid @RequestBody UsernameDTO username) {
        return userProfileService.changeUsername(userEmail, username);
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserProfileDTO updateAvatar(@PathVariable String userEmail, @RequestPart(value = "image") MultipartFile image) {
        return userProfileService.updateAvatar(userEmail, image);
    }

    @DeleteMapping("/avatar")
    public UserProfileDTO deleteAvatar(@PathVariable String userEmail) {
        return userProfileService.deleteAvatar(userEmail);
    }

}
