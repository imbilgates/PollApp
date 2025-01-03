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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
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


}
