package uk.co.jerryjane.cozyfarm.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        Long userId = null;
        String email;

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                userId = jwtService.getUserId(token);
                email = jwtService.getEmail(token);
                request.setAttribute("authUserId", userId);
                request.setAttribute("email", email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            } catch (JwtException ex) {
                System.out.println("JWT invalid: " + ex.getMessage());
            }
        }

        String path = request.getRequestURI();

        boolean isAdminEndpoint = path.startsWith("/api/admin/");
        boolean isLoginEndpoint = path.startsWith("/api/admin/auth/");

        if (isAdminEndpoint && !isLoginEndpoint && userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"error\":\"Missing or invalid token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
