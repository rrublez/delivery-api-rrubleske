package com.deliverytech.delivery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNomeRestauranteValidator.class)
public @interface UniqueNomeRestaurante {
  String message() default "Nome de restaurante já está registrado no sistema";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
