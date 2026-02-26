package com.example.sms.config;

import com.example.sms.util.SchoolContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SchoolContextFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        // /api/schools/SCH003/classes
        if (uri.startsWith("/api/schools/")) {
            String[] parts = uri.split("/");
            if (parts.length >= 4) {
                SchoolContextHolder.setSchoolCode(parts[3]);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            SchoolContextHolder.clear();
        }
    }
}