package com.bilgates.pollApp.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        chain.doFilter(request, response);

        // Modify the Set-Cookie header for JSESSIONID
        String header = httpServletResponse.getHeader("Set-Cookie");
        if (header != null && header.startsWith("JSESSIONID")) {
            httpServletResponse.setHeader("Set-Cookie", header + "; SameSite=None; Secure");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
