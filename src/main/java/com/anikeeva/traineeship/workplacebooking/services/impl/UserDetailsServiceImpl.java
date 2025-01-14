package com.anikeeva.traineeship.workplacebooking.services.impl;

import com.anikeeva.traineeship.workplacebooking.repositories.UserRepository;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с электронной почтой " + email + " не найден"));
        if (user.getIsDeleted()) {
            log.warn("User is deleted: {}", email);
            throw new DisabledException("Пользователь с электронной почтой " + email + " удален");
        }
        log.info("User loaded successfully: {}", email);
        return UserDetailsImpl.build(user);
    }
}
