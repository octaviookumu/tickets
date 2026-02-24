package com.octaviookumu.tickets.filters;

import com.octaviookumu.tickets.domain.User;
import com.octaviookumu.tickets.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// filters aren't a Spring concept, they're a servlet concept
@Component
@RequiredArgsConstructor
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    /**
     * insert this filter after spring security authentication takes place*
     * @param request     the request
     * @param response    the response
     * @param filterChain filterChain
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt jwt) {
            // extract the keycloak id. subject is user's id
            UUID keycloakId = UUID.fromString(jwt.getSubject());

            if (!userRepository.existsById(keycloakId)) {
                User user = new User();
                user.setId(keycloakId);
                user.setName(jwt.getClaimAsString("preferred_username"));
                user.setEmail(jwt.getClaimAsString("email"));
                userRepository.save(user);
            }

        }


        filterChain.doFilter(request, response);
    }
}
