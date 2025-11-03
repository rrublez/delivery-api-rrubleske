package com.deliverytech.delivery.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Resposta padronizada para erros da API.
 * 
 * Inclui apenas o campo 'trace' para erros 5xx (erros de servidor).
 * Para erros 4xx (validação), 'trace' não é incluído na resposta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    /**
     * Timestamp quando o erro ocorreu
     */
    private LocalDateTime timestamp;

    /**
     * Status HTTP (ex: 400, 404, 500)
     */
    private int status;

    /**
     * Mensagem de erro geral
     */
    private String message;

    /**
     * Caminho da requisição que causou o erro
     */
    private String path;

    /**
     * Stack trace do erro (incluído apenas para erros 5xx)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trace;

    /**
     * Detalhes de validação (para erros 400)
     * Map<campo, listaErros>
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, List<String>> validationErrors;

}
