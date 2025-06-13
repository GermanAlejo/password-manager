package com.passwordmanager.password_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CorsConfig corsConfig, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.corsConfig = corsConfig;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //This should change in prod
        http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable) //Disable for APIs (enable for forms)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/passwords/**").permitAll()
                                //.requestMatchers("/api/auth/**").permitAll() //allow this if we need to test without auth
                                .anyRequest())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
