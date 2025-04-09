package com.showtok.controller;

import com.showtok.dto.UserLoginDTO;
import com.showtok.dto.UserSignupDTO;
import com.showtok.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupDTO dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String name = body.get("name"); // 👈 프론트에서 사용자 이름도 같이 전달 필요
        String token = userService.loginOrRegisterGoogleUser(email, name);
        return ResponseEntity.ok(token);
    }
}
