package aChaushev.architects.validation;

import aChaushev.architects.validation.annotation.UniqueUserEmail;
import aChaushev.architects.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

  private final UserRepository userRepository;

  public UniqueUserEmailValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return userRepository
        .findByEmail(value)
        .isEmpty();
  }
}
