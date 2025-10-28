package com.deliverytech.delivery.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
            String application,
            String version,
            String developer,
            String javaVersion,
            String framework) {
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Delivery API",
                "javaVersion", System.getProperty("java.version"),
                "projectInfo",
                """
                        Imagine que você está no primeiro dia de trabalho em uma startup. O time de produto já definiu que o sistema deve:
                            • ⚖️ Ser escalável (começar pequeno, crescer grande)
                            • 🛠️ Usar tecnologias modernas e confiáveis (JDK 21 LTS)
                            • 🧑‍💻 Ter um ambiente de desenvolvimento padronizado
                            • 🤝 Permitir que outros desenvolvedores entrem no projeto facilmente
                        """);

    }

    @GetMapping("/info")
    public AppInfo info() {
        return new AppInfo(
                "Delivery Tech API",
                "1.0.0",
                "Rafael Rubleske",
                "JDK 21",
                "Spring Boot 3.4.11");

    }

}
