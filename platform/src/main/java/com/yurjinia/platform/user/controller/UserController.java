package com.yurjinia.platform.user.controller;

import com.yurjinia.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userEmail}")
public class UserController {

    private final UserService userService;

    /*
    ToDo: Refer to next JIRA with having more clarification about the reasons of
         why the code was commented, and when it's going to be uncommented:
         https://pashka1clash.atlassian.net/browse/YUR-114
    @PutMapping(value = "/{newUserEmail}")
    public ResponseEntity<Void> updateUserEmail(@PathVariable String userEmail, @Valid @RequestBody UpdateUserEmailRequest updateUserEmailRequest) {
        userService.updateUserEmail(userEmail, updateUserEmailRequest);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmUserEmail(@PathVariable String userEmail, @RequestParam("token") String token) {
        userService.confirmUserEmail(userEmail, token);
        return ResponseEntity.noContent().build();
    }*/

}
