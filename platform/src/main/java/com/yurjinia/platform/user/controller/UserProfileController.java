package com.yurjinia.platform.user.controller;

import com.yurjinia.platform.user.dto.UserProfileDTO;
import com.yurjinia.platform.user.service.UserProfileService;
import com.yurjinia.platform.user.dto.UpdateUserProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable String userEmail,
                                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                                            @Valid @RequestPart(value = "updateUserProfileRequest", required = false) UpdateUserProfileRequest updateUserProfileRequest) {
        UserProfileDTO userProfileDTO = userProfileService.updateUserProfile(userEmail, image, updateUserProfileRequest);

        return ResponseEntity.ok(userProfileDTO);
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<Void> deleteAvatar(@PathVariable String userEmail) {
        userProfileService.deleteAvatar(userEmail);

        return ResponseEntity.noContent().build();
    }

}
