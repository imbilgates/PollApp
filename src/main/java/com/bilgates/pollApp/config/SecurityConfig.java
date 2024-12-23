package com.bilgates.pollApp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity htttp) throws Exception {

        htttp
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/api/polls/**",
                                        "/oauth2/authorization/**",
                                        "/oauth2/**",
                                        "/user-info/**"
                                ).permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("http://localhost:3000/polls", true))
                .logout((logout) -> {
                    logout.logoutUrl("/logout");
                    logout.invalidateHttpSession(true);
                    logout.logoutSuccessUrl("http://localhost:3000/");
                    logout.clearAuthentication(true);
                    logout.deleteCookies("auth_code", "JSESSIONID", "refreshToken", "Authorization");
                });

        return htttp.build();
    }
}