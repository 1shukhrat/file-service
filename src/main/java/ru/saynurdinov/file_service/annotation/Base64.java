package ru.saynurdinov.file_service.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.saynurdinov.file_service.validator.Base64Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Base64Validator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64 {

    String message() default "The string is not Base64";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
