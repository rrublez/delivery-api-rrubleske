package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.repository.RestauranteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueNomeRestauranteValidator
    implements ConstraintValidator<UniqueNomeRestaurante, String> {

  @Autowired private RestauranteRepository restauranteRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }
    return !restauranteRepository.existsByNomeIgnoreCase(value);
  }
}
