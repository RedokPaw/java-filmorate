package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class FilmTest {
    static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testNameMustNotBeBlankAndDurationMustBePositive() {

        Film film = Film.builder()
                .name(" ")
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertEquals(violations.size(), 2);
    }
}
