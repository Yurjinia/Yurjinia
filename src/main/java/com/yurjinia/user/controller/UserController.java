package com.yurjinia.user.controller;

import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userEmail}")
public class UserController {

    private final UserService userService;

    @PutMapping(value = "/{newUserEmail}")
    public ResponseEntity<Void> updateUserEmail(@PathVariable String userEmail, @PathVariable String newUserEmail) {
        userService.updateUserEmail(userEmail, newUserEmail);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmUserEmail(@PathVariable String userEmail, @RequestParam("token") String token) {
        userService.confirmUserEmail(userEmail, token);
        return ResponseEntity.noContent().build();
    }

}
