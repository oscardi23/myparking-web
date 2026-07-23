package com.devsoft.myparking.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MenuInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            String path = request.getRequestURI();

            String activeMenu = "";

            if (path.startsWith("/admin/dashboard")){
                activeMenu = "dashboard";
            } else if (path.startsWith("/admin/clients")) {
                activeMenu = "clients";
            } else if (path.startsWith("/admin/reports")) {
                activeMenu = "reports";
            } else if (path.startsWith("/admin/vehicles")) {
                activeMenu = "vehicles";
            } else if (path.startsWith("/admin/operators")) {
                activeMenu = "operators";
            } else if (path.startsWith("/admin/entries")) {
                activeMenu = "entries";

            } else if (path.startsWith("/admin/parking")) {

                activeMenu = "parking";

            }

            modelAndView.addObject("activeMenu", activeMenu);
        }



    }
}
