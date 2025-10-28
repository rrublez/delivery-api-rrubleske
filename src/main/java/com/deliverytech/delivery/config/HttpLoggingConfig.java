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
            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime);

            log.info("=== INÍCIO DA REQUISIÇÃO ===");
            log.info("Método: {}", request.getMethod());
            log.info("URL: {}", request.getRequestURI());
            log.info("Query String: {}", request.getQueryString());
            log.info("Content-Type: {}", request.getContentType());
            log.info("Remote Address: {}", request.getRemoteAddr());

            return true;
        }

        @Override
        public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                @NonNull Object handler, @Nullable Exception ex) throws Exception {
            long startTime = (long) request.getAttribute("startTime");
            long duration = System.currentTimeMillis() - startTime;

            log.info("=== FIM DA REQUISIÇÃO ===");
            log.info("Status: {}", response.getStatus());
            log.info("Content-Type: {}", response.getContentType());
            log.info("Tempo de execução: {} ms", duration);
            
            if (ex != null) {
                log.error("Erro na requisição: ", ex);
            }
            
            log.info("=== ================================== ===");
        }
    }
}
