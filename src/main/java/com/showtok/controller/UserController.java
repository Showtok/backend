package com.showtok.controller;

import com.showtok.dto.UserProfileDTO;
import com.showtok.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PostMapping("/watch-ad")
    public ResponseEntity<String> rewardCredit() {
        userService.rewardCredit();
        return ResponseEntity.ok("광고 보상 완료! 크레딧 +1");
    }
}
