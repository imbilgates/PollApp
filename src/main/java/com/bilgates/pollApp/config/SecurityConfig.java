package com.bilgates.pollApp.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/user-info").authenticated()  // Specify that /user-info requires authentication
                                .anyRequest().permitAll())  // Allow all other requests without authentication
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl(frontendUrl+"/polls", true))
                .logout((logout) -> {
                    logout.logoutUrl("/logout");
                    logout.invalidateHttpSession(true);
                    logout.logoutSuccessUrl(frontendUrl+"/");
                    logout.clearAuthentication(true);
                    logout.deleteCookies("auth_code", "JSESSIONID", "refreshToken", "Authorization");
                });

        return http.build();
    }



    @Bean
    public ServletContextInitializer cookieInitializer(){
        return servletContext -> {
            servletContext.getSessionCookieConfig().setSecure(true);
            servletContext.getSessionCookieConfig().setHttpOnly(true);
        };
    }



    public void createSessionCookie(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        // Add the SameSite attribute manually
        response.addHeader("Set-Cookie",
                cookie.getName() + "=" + cookie.getValue() + "; Path=" + cookie.getPath() + "; HttpOnly; Secure; SameSite=None");
    }



}
