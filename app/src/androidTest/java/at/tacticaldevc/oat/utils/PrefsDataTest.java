package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashSet;
import java.util.Set;

import static at.tacticaldevc.oat.utils.Prefs.addNewOnStartupPermissionRequest;
import static at.tacticaldevc.oat.utils.Prefs.addTrustedContact;
import static at.tacticaldevc.oat.utils.Prefs.fetchOnStartupPermissionRequests;
import static at.tacticaldevc.oat.utils.Prefs.getAllTrustedContacts;
import static at.tacticaldevc.oat.utils.Prefs.removeOnStartupPermissionRequest;
import static at.tacticaldevc.oat.utils.Prefs.removeTrustedContact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PrefsDataTest {
    private static final String DOCUMENT_NAME_TEST = "oat-data";
    private final static String KEY_TRUSTED_CONTACTS = "trusted-contacts";
    private final static String KEY_MISSING_PERMISSIONS_TO_REQUEST_ON_STARTUP = "missing-permission";

    @Before
    public void init() {
        clean();
    }

    @Test
    public void getAllTrustedContactsWithData() {
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);

        // prepare
        Set<String> numbers = trustedContactsSetup();

        // test
        Set<String> result = getAllTrustedContacts(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEqualTo(numbers);
    }

    @Test
    public void getAllTrustedContactsWithoutData() {
        // test
        Set<String> result = getAllTrustedContacts(InstrumentationRegistry.getInstrumentation().getTargetContext());
        // assert
        assertThat(result).isEmpty();
    }


    @Test
    public void addTrustedContactsWithValidData() {
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
    public void addTrustedContactsWithNull() {
        // prepare
        String number = null;

        // assert
        assertThrows(IllegalArgumentException.class, () -> addTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), number));
    }

    @Test
    public void addTrustedContactsWithInvalidPhoneNumbers() {
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
    public void removeTrustedContactWithValidPhoneNumber() {
        // prepare
        Set<String> numbers = trustedContactsSetup();
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
    public void removeTrustedContactWithUnknownNumber() {
        // prepare
        Set<String> numbers = trustedContactsSetup();
        String unknownNumber = "0664 677 00000001";

        // test
        String result = removeTrustedContact(InstrumentationRegistry.getInstrumentation().getTargetContext(), unknownNumber);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_TRUSTED_CONTACTS, new HashSet<>());

        assertThat(load).isEqualTo(numbers);
        assertThat(result).isNull();
    }

    @Test
    public void addNewOnStartupPermissionRequestWithValidValueAndExistingData() {
        // prepare
        Set<String> setup = onStartupPermissionPermissionRequestSetup();
        String missingPermission = "ACCESS_COARSE_LOCATION";

        // test
        String result = addNewOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), missingPermission);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_MISSING_PERMISSIONS_TO_REQUEST_ON_STARTUP, new HashSet<>());
        setup.add(missingPermission);

        assertThat(result).isSameAs(missingPermission);
        assertThat(load).isEqualTo(setup);
    }

    @Test
    public void addNewOnStartupPermissionRequestWithValidValueWithoutExistingData() {
        // prepare
        String missingPermission = "ACCESS_COARSE_LOCATION";
        Set<String> expectedResult = new HashSet<>();
        expectedResult.add(missingPermission);

        // test
        String result = addNewOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), missingPermission);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_MISSING_PERMISSIONS_TO_REQUEST_ON_STARTUP, new HashSet<>());
        expectedResult.add(missingPermission);

        assertThat(result).isSameAs(missingPermission);
        assertThat(load).isEqualTo(expectedResult);
    }

    @Test
    public void addNewOnStartupPermissionRequestWithNullValues() {
        // assert
        assertThrows(IllegalArgumentException.class, () -> addNewOnStartupPermissionRequest(null, null));
        assertThrows(IllegalArgumentException.class, () -> addNewOnStartupPermissionRequest(null, "missing permission"));
        assertThrows(IllegalArgumentException.class, () -> addNewOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), null));
    }

    @Test
    public void addNewOnStartupPermissionRequestWithInvalidPermissions() {
        // prepare
        String invalidPermission1 = "";
        String invalidPermission2 = " ";
        String invalidPermission3 = "\r\n";

        // assert
        assertThrows(IllegalArgumentException.class, () -> addNewOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), invalidPermission1));
        assertThrows(IllegalArgumentException.class, () -> addNewOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), invalidPermission2));
        assertThrows(IllegalArgumentException.class, () -> addNewOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), invalidPermission3));
    }

    @Test
    public void removeOnStartupPermissionRequestWithValidValuesAndExistingData() {
        // prepare
        Set<String> setup = onStartupPermissionPermissionRequestSetup();
        String grantedPermission = "SEND_SMS";

        // test
        String result = removeOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), grantedPermission);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_MISSING_PERMISSIONS_TO_REQUEST_ON_STARTUP, new HashSet<>());
        setup.remove(grantedPermission);

        assertThat(result).isSameAs(grantedPermission);
        assertThat(load).isEqualTo(setup);
    }

    @Test
    public void removeOnStartupPermissionRequestWithValidValuesWithoutExistingData() {
        // prepare
        String missingPermission = "ACCESS_COARSE_LOCATION";

        // test
        String result = removeOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), missingPermission);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Set<String> load = prefs.getStringSet(KEY_MISSING_PERMISSIONS_TO_REQUEST_ON_STARTUP, new HashSet<>());

        assertThat(result).isSameAs(missingPermission);
        assertThat(load).isEmpty();
    }

    @Test
    public void removeOnStartupPermissionRequestWithNullValues() {
        // assert
        assertThrows(IllegalArgumentException.class, () -> removeOnStartupPermissionRequest(null, null));
        assertThrows(IllegalArgumentException.class, () -> removeOnStartupPermissionRequest(null, "missing permission"));
        assertThrows(IllegalArgumentException.class, () -> removeOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), null));
    }

    @Test
    public void removeOnStartupPermissionRequestWithInvalidPermissions() {
        // prepare
        String invalidPermission1 = "";
        String invalidPermission2 = " ";
        String invalidPermission3 = "\r\n";

        // assert
        assertThrows(IllegalArgumentException.class, () -> removeOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), invalidPermission1));
        assertThrows(IllegalArgumentException.class, () -> removeOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), invalidPermission2));
        assertThrows(IllegalArgumentException.class, () -> removeOnStartupPermissionRequest(InstrumentationRegistry.getInstrumentation().getTargetContext(), invalidPermission3));
    }

    @Test
    public void fetchOnStartupPermissionRequestsWithExistingValues() {
        // prepare
        Set<String> setup = onStartupPermissionPermissionRequestSetup();

        // test
        Set<String> result = fetchOnStartupPermissionRequests(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEqualTo(setup);
    }

    @Test
    public void fetchOnStartupPermissionRequestWithoutData() {
        // test
        Set<String> result = fetchOnStartupPermissionRequests(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void fetchOnStartupPermissionRequestWithNullValues() {
        // assert
        assertThrows(IllegalArgumentException.class, () -> fetchOnStartupPermissionRequests(null));
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

    private Set<String> trustedContactsSetup() {
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        HashSet<String> numbers = new HashSet<>();
        numbers.add("+43 677 00000000");
        numbers.add("0664 677 00000000");
        editor.putStringSet(KEY_TRUSTED_CONTACTS, numbers);
        editor.apply();

        return numbers;
    }

    private Set<String> onStartupPermissionPermissionRequestSetup() {
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        HashSet<String> missingPermissions = new HashSet<>();
        missingPermissions.add("SEND_SMS");
        missingPermissions.add("RECEIVE_SMS");
        editor.putStringSet(KEY_MISSING_PERMISSIONS_TO_REQUEST_ON_STARTUP, missingPermissions);
        editor.apply();

        return missingPermissions;
    }
}