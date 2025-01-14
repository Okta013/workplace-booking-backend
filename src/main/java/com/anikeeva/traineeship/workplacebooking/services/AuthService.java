package com.anikeeva.traineeship.workplacebooking.services;

import com.anikeeva.traineeship.workplacebooking.exceptions.AuthenticationFailedException;
import com.anikeeva.traineeship.workplacebooking.repositories.UserRepository;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.jwt.JwtUtils;
import com.anikeeva.traineeship.workplacebooking.payload.request.LoginRequest;
import com.anikeeva.traineeship.workplacebooking.payload.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        log.info("Authenticating user with email: {}", loginRequest.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        if (authentication == null) {
            throw new AuthenticationFailedException("Ошибка аутентификации пользователя с электронной почтой: " + loginRequest.email());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь с электронной почтой" + userDetails.getUsername() + " не найден"));

        if (user.isPasswordNeedsChange()) {
            log.info("User '{}' needs to change password", userDetails.getUsername());
        }

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        log.info("User '{}' authenticated successfully with roles: {}", userDetails.getUsername(), roles);

        return new JwtResponse(token, userDetails.getUsername(), roles);
    }
}
