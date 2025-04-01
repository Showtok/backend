package com.showtok.service;

import com.showtok.config.JwtTokenProvider;
import com.showtok.domain.Role;
import com.showtok.domain.User;
import com.showtok.dto.UserLoginDTO;
import com.showtok.dto.UserProfileDTO;
import com.showtok.dto.UserSignupDTO;
import com.showtok.repository.RoleRepository;
import com.showtok.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public void signup(UserSignupDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("이미 존재하는 아이디입니다");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다"));

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .roles(Collections.singleton(userRole))
                .credit(0) // ⭐ 초기 크레딧 0
                .build();

        userRepository.save(user);
    }

    public String login(UserLoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        return tokenProvider.generateToken(authentication);
    }

    public UserProfileDTO getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));

        return UserProfileDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .credit(user.getCredit()) // 💳 크레딧 포함
                .build();
    }

    @Transactional
    public void rewardCredit() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));

        user.setCredit(user.getCredit() + 1); // 광고 시청 시 1크레딧 추가
    }
}
