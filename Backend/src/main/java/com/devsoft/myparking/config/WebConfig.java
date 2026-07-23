package com.devsoft.myparking.config;

import com.devsoft.myparking.interceptor.MenuInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MenuInterceptor()).addPathPatterns("/admin/**");
    }
}
