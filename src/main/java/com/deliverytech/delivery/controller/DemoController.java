package com.deliverytech.delivery.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Controller de demonstração dos recursos modernos do Java 21
 * - Pattern Matching (Java 17+)
 * - Virtual Threads (Java 21)
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    /**
     * Demonstra Pattern Matching com instanceof
     * Endpoint: GET /api/demo/pattern-matching?type=string&value=hello
     */
    @GetMapping("/pattern-matching")
    public Map<String, Object> demonstratePatternMatching(
            @RequestParam String type,
            @RequestParam String value) {

        Object obj = convertToType(type, value);
        String result = analyzeObject(obj);

        return Map.of(
                "input", value,
                "type", type,
                "convertedType", obj.getClass().getSimpleName(),
                "analysis", result,
                "feature", "Pattern Matching (Java 17+)");

    }

    /**
     * Demonstra Virtual Threads processando múltiplas tarefas simultaneamente
     * Endpoint: GET /api/demo/virtual-threads?tasks=5
     */
    @GetMapping("/virtual-threads")
    public Map<String, Object> demonstrateVirtualThreads(
            @RequestParam(defaultValue = "5") int tasks) {

        Instant start = Instant.now();

        // Usando Virtual Threads (Java 21)
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < tasks; i++) {
                final int taskId = i + 1;
                executor.submit(() -> {
                    log.info("Virtual Thread {} iniciada", taskId);
                    simulateWork(taskId);
                    log.info("Virtual Thread {} finalizada", taskId);

                });

            }

        } // Auto-shutdown e aguarda todas as threads

        Duration duration = Duration.between(start, Instant.now());

        return Map.of(
                "feature", "Virtual Threads (Java 21)",
                "tasksExecuted", tasks,
                "executionTime", duration.toMillis() + "ms",
                "description", "Cada tarefa simulou 100ms de trabalho em paralelo",
                "benefit", "Virtual threads são leves e permitem alta concorrência");

    }

    /**
     * Compara Virtual Threads com Platform Threads
     * Endpoint: GET /api/demo/threads-comparison?tasks=10
     */
    @GetMapping("/threads-comparison")
    public Map<String, Object> compareThreads(
            @RequestParam(defaultValue = "10") int tasks) {

        // Teste com Virtual Threads
        Instant startVirtual = Instant.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < tasks; i++) {
                final int taskId = i;
                executor.submit(() -> simulateWork(taskId));

            }

        }
        long virtualTime = Duration.between(startVirtual, Instant.now()).toMillis();

        // Teste com Platform Threads (thread pool fixo)
        Instant startPlatform = Instant.now();
        try (var executor = Executors.newFixedThreadPool(Math.min(tasks, 10))) {
            for (int i = 0; i < tasks; i++) {
                final int taskId = i;
                executor.submit(() -> simulateWork(taskId));

            }

        }
        long platformTime = Duration.between(startPlatform, Instant.now()).toMillis();

        return Map.of(
                "tasks", tasks,
                "virtualThreadsTime", virtualTime + "ms",
                "platformThreadsTime", platformTime + "ms",
                "winner", virtualTime <= platformTime ? "Virtual Threads" : "Platform Threads",
                "improvement", String.format("%.1f%%",
                        ((double) (platformTime - virtualTime) / platformTime) * 100));

    }

    // === Métodos auxiliares ===

    /**
     * Pattern Matching em ação - análise de tipo com switch expression
     */
    private String analyzeObject(Object obj) {
        return switch (obj) {
            case Integer i when i > 0 ->
                "Número inteiro positivo: " + i;

            case Integer i when i < 0 ->
                "Número inteiro negativo: " + i;

            case Integer i ->
                "Número inteiro zero: " + i;

            case Double d when d > 0.0 ->
                "Número decimal positivo: " + d;

            case Double d ->
                "Número decimal não-positivo: " + d;

            case String s when s.isEmpty() ->
                "String vazia";

            case String s ->
                "String com " + s.length() + " caracteres: '" + s + "'";

            case null ->
                "Valor nulo";

            default ->
                "Tipo desconhecido: " + obj.getClass().getSimpleName();

        };

    }

    /**
     * Converte string para diferentes tipos
     */
    private Object convertToType(String type, String value) {
        return switch (type.toLowerCase()) {
            case "int", "integer" -> Integer.parseInt(value);
            case "double", "decimal" -> Double.parseDouble(value);
            case "string" -> value;
            default -> value;

        };

    }

    /**
     * Simula trabalho pesado (I/O, processamento, etc)
     */
    private void simulateWork(int taskId) {
        try {
            Thread.sleep(100); // Simula operação I/O

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }

    }

}
