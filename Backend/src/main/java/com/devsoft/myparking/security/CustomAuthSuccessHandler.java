package com.devsoft.myparking.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


            String role = authentication.getAuthorities()
                    .iterator().next().getAuthority();

            switch (role) {
                case "ROLE_SUPER_ADMIN" -> response.sendRedirect("/super/dashboard");
                case "ROLE_ADMIN"       -> response.sendRedirect("/admin/dashboard");
                case "ROLE_OPERATOR"    -> response.sendRedirect("/operator/dashboard");
                default                 -> response.sendRedirect("/auth/login");
            }


    }
}
