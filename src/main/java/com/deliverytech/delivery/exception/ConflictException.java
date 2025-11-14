package com.deliverytech.delivery.exception;

/**
 * Exceção lançada quando há conflito nos dados (409 Conflict)
 * Ex: Email ou CNPJ já registrado, operação inválida para o estado atual
 */
public class ConflictException extends RuntimeException {
  public ConflictException(String message) {
    super(message);
  }

  public ConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
