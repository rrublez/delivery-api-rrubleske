package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.repository.ClienteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueCpfValidator implements ConstraintValidator<UniqueCpf, String> {

  @Autowired private ClienteRepository clienteRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }
    return !clienteRepository.existsByCpf(value);
  }
}
