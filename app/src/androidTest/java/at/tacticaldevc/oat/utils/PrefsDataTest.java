package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashSet;
import java.util.Set;

import static at.tacticaldevc.oat.utils.Prefs.addTrustedContact;
import static at.tacticaldevc.oat.utils.Prefs.getAllTrustedContacts;
import static at.tacticaldevc.oat.utils.Prefs.removeTrustedContact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PrefsDataTest {
    private static final String DOCUMENT_NAME_TEST = "oat-data";
    private final static String KEY_TRUSTED_CONTACTS = "trusted-contacts";

    @Before
    public void init() {
        clean();
    }

    @Test
    public void testGetAllTrustedContacts() {
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);

        // prepare
        Set<String> numbers = setup();

        // test
        Set<String> result = getAllTrustedContacts(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEqualTo(numbers);
    }

    @Test
    public void testGetAllTrustedContactsWithoutData() {
        // test
        Set<String> result = getAllTrustedContacts(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // assert
        assertThat(result).isEmpty();
    }


    @Test
    public void positiveTestAddTrustedContacts() {
        // prepare
        String number1 = "+43 677 00000000";
        String number2 = "0664 677 00000000";

        // test
        String result1 = addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number1);
        String result2 = addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number2);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> numbers = prefs.getStringSet(KEY_TRUSTED_CONTACTS, new HashSet<>());

        assertThat(numbers).contains(number1, number2);
        assertThat(result1).isSameAs(number1);
        assertThat(result2).isSameAs(number2);
    }

    @Test
    public void negativeTestAddTrustedContactsWithNull() {
        // prepare
        String number = null;

        // assert
        assertThrows(IllegalArgumentException.class, () -> addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number));
    }

    @Test
    public void negativeTestAddTrustedContactsWithInvalidPhoneNumbers() {
        // prepare
        String number1 = "not a number";
        String number2 = " ";
        String number3 = "";

        // assert
        assertThrows(IllegalArgumentException.class, () -> addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number1));
        assertThrows(IllegalArgumentException.class, () -> addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number2));
        assertThrows(IllegalArgumentException.class, () -> addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number3));
    }


    @Test
    public void positiveTestRemoveTrustedContact() {
        // prepare
        Set<String> numbers = setup();
        String number = "+43 677 00000000";

        // test
        String result = removeTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number);

        // assert
        numbers.remove(number);
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_TRUSTED_CONTACTS, new HashSet<>());

        assertThat(load).isEqualTo(numbers);
        assertThat(result).isSameAs(number);
    }

    @Test
    public void positiveTestRemoveTrustedContactWithUnknownNumber() {
        // prepare
        Set<String> numbers = setup();
        String unknownNumber = "0664 677 00000001";

        // test
        String result = removeTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), unknownNumber);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_TRUSTED_CONTACTS, new HashSet<>());

        assertThat(load).isEqualTo(numbers);
        assertThat(result).isNull();
    }

    @AfterEach
    public void cleanup() {
        clean();
    }

    private void clean() {
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    private Set<String> setup() {
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        HashSet<String> numbers = new HashSet<>();
        numbers.add("+43 677 00000000");
        numbers.add("0664 677 00000000");
        editor.putStringSet(KEY_TRUSTED_CONTACTS, numbers);
        editor.commit();

        return numbers;
    }
}