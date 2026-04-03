package com.devsoft.myparking.config;

import com.devsoft.myparking.security.CustomAuthSuccessHandler;
import com.devsoft.myparking.security.CustomUserDetails;
import com.devsoft.myparking.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;


@Configuration

@RequiredArgsConstructor


public class SecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;



    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {


        http
                .csrf(csrf -> csrf.disable())
                .requestCache(cache -> cache.disable())
                .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/super/**").hasRole("SUPER_ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/operator/**").hasRole("OPERATOR")

                // endpoint public
                                .requestMatchers(

                                        "/auth/register",
                                        "/auth/send-code",
                                        "/auth/verify-code",
                                        "/auth/complete-register",
                                        "/auth/forgot-password",
                                        "/auth/reset-password",
                                        "/login",
                                        "/auth/login",
                                        "/js/**",
                                        "/css/**",
                                        "/img/**",
                                        "/auth/error",
                                        "/favicon.ico"
                                ).permitAll()

                                .anyRequest().authenticated()


                )

                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .successHandler(customAuthSuccessHandler)
                       .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                )
                .requestCache(cache -> cache.disable())

                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                        {
                            response.sendRedirect("/auth/login");
                        })
                );

        return http.build();

    }







}

