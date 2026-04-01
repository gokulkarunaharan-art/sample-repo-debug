package com.gokul.librarymanagement.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println("Inside the Filter... request uri is-- "+request.getRequestURI());
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
