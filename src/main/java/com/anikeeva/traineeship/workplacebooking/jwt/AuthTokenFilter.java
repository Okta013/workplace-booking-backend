package com.anikeeva.traineeship.workplacebooking.jwt;

import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsImpl;
import com.anikeeva.traineeship.workplacebooking.services.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("AuthTokenFilter: Starting filter chain for request: {} method: {}", request.getRequestURI(), request.getMethod());
        if ("OPTIONS".equals(request.getMethod())) {
            log.info("AuthTokenFilter: OPTIONS request, skipping authentication");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (!("/login".equals(uri) && "POST".equals(method))) {
            String jwt = parseJwt(request);
        } else {
            filterChain.doFilter(request, response);
        }
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                log.info("Parsed JWT token, username: {}", username);

                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

                if (userDetails.isPasswordNeedsChange()) {
                    log.warn("Password change is required for user {}", username);
                    String requestURI = request.getRequestURI();

                    if (!requestURI.equals("/users/changePassword") && !requestURI.equals("/dashboard") &&
                            !requestURI.startsWith("/users/profile")) {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Password change required.");
                        return;
                    }
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication", e);
        }
        log.info("AuthTokenFilter: Filter chain completed for request: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
