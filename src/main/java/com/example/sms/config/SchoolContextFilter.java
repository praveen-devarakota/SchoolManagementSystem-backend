package com.example.sms.config;

import com.example.sms.util.SchoolContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SchoolContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String uri = request.getRequestURI();
            // /api/schools/SCH003/classes
            if (uri.startsWith("/api/schools/")) {
                String[] parts = uri.split("/");
                if (parts.length >= 4) {
                    SchoolContextHolder.setSchoolCode(parts[3]);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            SchoolContextHolder.clear();
        }
    }
}