package com.deliverytech.delivery.validation;

import com.deliverytech.delivery.repository.RestauranteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueCnpjValidator implements ConstraintValidator<UniqueCnpj, String> {

  @Autowired private RestauranteRepository restauranteRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }
    return !restauranteRepository.existsByCnpj(value);
  }
}
