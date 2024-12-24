package com.bilgates.pollApp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SameSiteCookieFilter> sameSiteCookieFilter() {
        FilterRegistrationBean<SameSiteCookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SameSiteCookieFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}