package com.example.UniversityManagementSystem.security;

import com.example.UniversityManagementSystem.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * This class is to implement the Jwt filer into the security filter chain.
 * It will use the JwtUtil to validate the token and extract the username from the token.
 * And if the authentication is successful, it will set the authentication in the security context for the current request.
 */

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("======> JWT Filter executing for request: {} {}", request.getMethod(), request.getRequestURI());

        // 1. Look for the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. No token? Skip this filter entirely — pass request forward
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header not present");
            log.info("Skipping JWT filter for this request");
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Strip "Bearer " prefix to get the raw token
        String token = authHeader.substring(7);

        // 4. Extract the username baked into the token
        String username = jwtUtil.extractUsername(token);

        // 5. Only proceed if we got a username AND no one is already authenticated
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load the full user from the database (to get roles)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 7. Verify the token is genuine and not expired
            if (jwtUtil.isTokenValid(token, userDetails)) {
                log.info("JWT valid for user: {}", username);

                // 8. Build an Authentication object — user is now verified
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,                 // no credentials needed
                                userDetails.getAuthorities()   // their roles
                        );

                // 9. Pin it to the notice board for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("JWT pinned in SecurityContextHolder for user: {}", username);
            }
        }

        // 10. Pass the request to the next filter in the chain
        log.info("Passing request to next filter in chain");
        filterChain.doFilter(request, response);
    }
}