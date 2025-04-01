package com.showtok.config;

import com.showtok.domain.Role;
import com.showtok.domain.User;
import com.showtok.repository.RoleRepository;
import com.showtok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        userRepository.findByUsername(email).orElseGet(() -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER가 존재하지 않습니다"));
            User user = User.builder()
                    .username(email)
                    .password("oauth") // 의미 없는 값
                    .nickname(nickname)
                    .phone("000-0000-0000") // 기본값
                    .roles(Collections.singleton(userRole))
                    .build();
            return userRepository.save(user);
        });

        return oAuth2User;
    }
}
