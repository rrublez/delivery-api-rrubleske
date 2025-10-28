package com.deliverytech.delivery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class HttpLoggingConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new HttpLoggingInterceptor());
    }

    public static class HttpLoggingInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) 
                throws Exception {
            request.setAttribute("startTime", System.currentTimeMillis());
            log.info("→ {} {}", request.getMethod(), request.getRequestURI());
            return true;
        }

        @Override
        public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                @NonNull Object handler, @Nullable Exception ex) throws Exception {
            long duration = System.currentTimeMillis() - (long) request.getAttribute("startTime");
            
            if (ex != null) {
                log.error("← {} {} [{}] {}ms - ERROR: {}", 
                    request.getMethod(), request.getRequestURI(), 
                    response.getStatus(), duration, ex.getMessage());
            } else {
                log.info("← {} {} [{}] {}ms", 
                    request.getMethod(), request.getRequestURI(), 
                    response.getStatus(), duration);
            }
        }
    }
}
