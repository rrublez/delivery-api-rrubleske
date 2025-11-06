package com.deliverytech.delivery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCpfValidator.class)
public @interface UniqueCpf {
  String message() default "CPF já está registrado no sistema";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
