package ru.saynurdinov.file_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.saynurdinov.file_service.annotation.Base64;

public class Base64Validator implements ConstraintValidator<Base64, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            java.util.Base64.getDecoder().decode(s);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
