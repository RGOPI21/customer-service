package com.bank.customerservice.security;

import com.bank.customerservice.util.AuthenticatedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor  // ✅ This automatically creates constructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private static final ThreadLocal<AuthenticatedUser> userHolder = new ThreadLocal<>();

    // ❌ REMOVE THIS MANUAL CONSTRUCTOR - @RequiredArgsConstructor already creates it
    // public JwtAuthInterceptor(JwtUtils jwtUtils) {
    //     this.jwtUtils = jwtUtils;
    // }

    public static AuthenticatedUser getCurrentUser() {
        return userHolder.get();
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // ✅ Allow CORS preflight requests
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtils.validateToken(token)) {
                AuthenticatedUser user = jwtUtils.extractUser(token);
                userHolder.set(user);
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized or invalid token");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        userHolder.remove();
    }
}