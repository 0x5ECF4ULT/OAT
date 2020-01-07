package at.tacticaldevc.oat.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnsurerTest {

    @ParameterizedTest(name = "`{0}`")
    @ValueSource(strings = {"object", "teststring", "1", " ", ""})
    void testEnsureNotNullWithCorrectValues(Object object) {
        // test
        Object result = ensureNotNull(object);

        // assert
        assertThat(result).isSameAs(object);
    }

    @Test
    void testEnsureNotNullWithNullValues() {
        assertThrows(IllegalArgumentException.class, () -> ensureNotNull(null));
        assertThrows(IllegalArgumentException.class, () -> ensureNotNull((String) null));
        assertThrows(IllegalArgumentException.class, () -> ensureNotNull(null), "testValue");
        assertThrows(IllegalArgumentException.class, () -> ensureNotNull((String) null), "testValue");
    }

    @ParameterizedTest
    @ValueSource(strings = {"string", "testString", "1"})
    void testEnsureStringIsValidWithCorrectValues(String value) {
        // test
        String result1 = ensureStringIsValid(value);
        String result2 = ensureStringIsValid(value, "testValue");

        // assert
        assertThat(result1).isSameAs(value);
        assertThat(result2).isSameAs(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\r", "\n\r"})
    void testEnsureStringIsValidWithIncorrectValues(String value) {
        assertThrows(IllegalArgumentException.class, () -> ensureStringIsValid(value));
        assertThrows(IllegalArgumentException.class, () -> ensureStringIsValid(value, "testValue"));
    }

}