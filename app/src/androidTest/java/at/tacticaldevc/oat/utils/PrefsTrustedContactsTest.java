package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static at.tacticaldevc.oat.utils.Prefs.deleteTrustedContact;
import static at.tacticaldevc.oat.utils.Prefs.fetchTrustedContacts;
import static at.tacticaldevc.oat.utils.Prefs.saveTrustedContact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrefsTrustedContactsTest {
    private static final String DOCUMENT_NAME_TEST = "oat-trusted-contacts";

    @Before
    public void init() {
        clean();
    }

    // Trusted contacts
    @Test
    public void getAllTrustedContactsWithData() {
        // prepare
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, String> numbers = trustedContactsSetup();

        // test
        Map<String, String> result = fetchTrustedContacts(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEqualTo(numbers);
    }

    @Test
    public void getAllTrustedContactsWithoutData() {
        // test
        Map<String, String> result = fetchTrustedContacts(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void saveTrustedContactsWithValidData() {
        // prepare
        String number1 = "+43 677 00000000";
        String number2 = "0664 677 00000000";

        // test
        String result1 = saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number1, "+43 number");
        String result2 = saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number2, "0664 number");

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, String> loaded = new HashMap<>();
        Set<String> keys = prefs.getAll().keySet();
        for (String key : keys) {
            loaded.put(key, prefs.getString(key, null));
        }

        assertThat(loaded.getOrDefault(number1, null)).isNotNull();
        assertThat(loaded.getOrDefault(number2, null)).isNotNull();
        assertThat(result1).isSameAs(number1);
        assertThat(result2).isSameAs(number2);
    }

    @Test
    public void saveTrustedContactsWithInvalidValues() {
        // assert
        assertThrows(IllegalArgumentException.class, () -> saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), null, "test"));
        assertThrows(IllegalArgumentException.class, () -> saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), "", "test"));
        assertThrows(IllegalArgumentException.class, () -> saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), " ", "test"));
        assertThrows(IllegalArgumentException.class, () -> saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), "not a number", "test"));
        assertThrows(IllegalArgumentException.class, () -> saveTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), "+43 677 00000000", ""));
        assertThrows(IllegalArgumentException.class, () -> saveTrustedContact(null, "+43 677 00000000", "test"));
    }

    @Test
    public void removeTrustedContactWithValidPhoneNumber() {
        // prepare
        Map<String, String> numbers = trustedContactsSetup();
        String number = "+43 677 00000000";

        // test
        String result = deleteTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number);

        // assert
        numbers.remove(number);
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, String> loaded = new HashMap<>();
        Set<String> keys = prefs.getAll().keySet();
        for (String key : keys) {
            loaded.put(key, prefs.getString(key, null));
        }

        assertThat(loaded).isEqualTo(numbers);
        assertThat(result).isSameAs(number);
    }

    @Test
    public void removeTrustedContactWithUnknownNumber() {
        // prepare
        Map<String, String> numbers = trustedContactsSetup();
        String unknownNumber = "0664 677 00000001";

        // test
        String result = deleteTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), unknownNumber);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, String> loaded = new HashMap<>();
        Set<String> keys = prefs.getAll().keySet();
        for (String key : keys) {
            loaded.put(key, prefs.getString(key, null));
        }

        assertThat(loaded).isEqualTo(numbers);
        assertThat(result).isSameAs(unknownNumber);
    }

    @AfterEach
    public void cleanup() {
        clean();
    }

    private void clean() {
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    private HashMap<String, String> trustedContactsSetup() {
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        HashMap<String, String> numbers = new HashMap<>();
        editor.putString("+43 677 00000000", "test1");
        numbers.put("+43 677 00000000", "test1");
        editor.putString("0664 677 00000000", "test2");
        numbers.put("0664 677 00000000", "test2");

        editor.apply();

        return numbers;
    }
}
