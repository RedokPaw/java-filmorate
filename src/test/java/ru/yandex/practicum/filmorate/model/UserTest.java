package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

public class UserTest {
    static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void userEmailMustBeWellFormedAndBirthdayMustBeInPastAndLoginMustNotBeBlank() {
        User user = User.builder()
                .email("adasda.ru")
                .birthday(LocalDate.of(2044, 1, 1))
                .login("")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertEquals(violations.size(), 3);

    }

}
