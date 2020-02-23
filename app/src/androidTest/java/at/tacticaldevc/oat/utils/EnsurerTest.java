package at.tacticaldevc.oat.utils;

import org.junit.Test;

import static at.tacticaldevc.oat.utils.Ensurer.ensurePhoneNumberIsValid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnsurerTest {


    @Test
    public void ensurePhoneNumberIsValidWithCorrectValues() {
        String value1 = "+43 677 00000000";
        String value2 = "0664 677 00000000";
        // test
        String result1 = ensurePhoneNumberIsValid(value1, "testValue");
        String result2 = ensurePhoneNumberIsValid(value2, "testValue");
        // assert
        assertThat(result1).isSameAs(value1);
        assertThat(result2).isSameAs(value2);
    }

    @Test
    public void ensurePhoneNumberIsValidWithIncorrectValues() {
        // prepare
        String value1 = "no phone number";
        String value2 = " ";
        String value3 = "";
        String value4 = "testNumber";
        String value5 = "0";

        // assert
        assertThrows(IllegalArgumentException.class, () -> ensurePhoneNumberIsValid(value1, "testValue"));
        assertThrows(IllegalArgumentException.class, () -> ensurePhoneNumberIsValid(value2, "testValue"));
        assertThrows(IllegalArgumentException.class, () -> ensurePhoneNumberIsValid(value3, "testValue"));
        assertThrows(IllegalArgumentException.class, () -> ensurePhoneNumberIsValid(value4, "testValue"));
        assertThrows(IllegalArgumentException.class, () -> ensurePhoneNumberIsValid(value5, "testValue"));
    }

}