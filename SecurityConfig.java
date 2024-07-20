package com.example.demo.Controller;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final HikariDataSource dataSource;

    public SecurityConfig(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 접근 허용
                        .requestMatchers("/", "/login/**", "/sign_up/**").permitAll()
                        .requestMatchers("/posts/**", "/api/v1/posts/**").hasRole(Role.USER.name())
                        .requestMatchers("/admins/**", "/api/v1/admins/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 콘솔을 위한 설정
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("1234"))
                .roles(Role.ADMIN.name())
                .build();

        UserDetails guest = User.withUsername("guest")
                .password(passwordEncoder().encode("guest"))
                .roles(Role.GUEST.name())
                .build();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user"))
                .roles(Role.USER.name())
                .build();

        return new InMemoryUserDetailsManager(admin, guest, user);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DataSource를 사용하는 방법으로 변경
    @Bean
    public UserDetailsService jdbcUserDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    public enum Role {
        USER, ADMIN, GUEST;
    }

}
