package com.deliverytech.delivery.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Exceção para erros de negócio da aplicação.
 * Permite especificar um status HTTP customizado.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BusinessException(String message) {
        this(message, HttpStatus.CONFLICT);
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Throwable cause) {
        this(message, HttpStatus.CONFLICT, cause);
    }

    public BusinessException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
