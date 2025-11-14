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
        registry.addInterceptor(new HttpLoggingInterceptor())
            .excludePathPatterns(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/webjars/**"
            );
    }

    public static class HttpLoggingInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) 
                throws Exception {
            try {
                request.setAttribute("startTime", System.currentTimeMillis());
                String method = request.getMethod() != null ? request.getMethod() : "UNKNOWN";
                String uri = request.getRequestURI() != null ? request.getRequestURI() : "";
                log.info("→ {} {}", method, uri);
            } catch (Exception e) {
                log.debug("Error logging request preHandle", e);
            }
            return true;
        }

        @Override
        public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                @NonNull Object handler, @Nullable Exception ex) throws Exception {
            try {
                Object startTimeObj = request.getAttribute("startTime");
                long duration = startTimeObj != null ? System.currentTimeMillis() - (long) startTimeObj : 0;
                
                String method = request.getMethod() != null ? request.getMethod() : "UNKNOWN";
                String uri = request.getRequestURI() != null ? request.getRequestURI() : "";
                int status = response.getStatus();
                String errorMsg = ex != null ? ex.getMessage() : null;
                
                if (ex != null && errorMsg != null) {
                    log.error("← {} {} [{}] {}ms - ERROR: {}", 
                        method, uri, status, duration, errorMsg);
                } else if (ex != null) {
                    log.error("← {} {} [{}] {}ms - ERROR", 
                        method, uri, status, duration);
                } else {
                    log.info("← {} {} [{}] {}ms", 
                        method, uri, status, duration);
                }
            } catch (Exception e) {
                log.debug("Error logging request afterCompletion", e);
            }
        }
    }
}
