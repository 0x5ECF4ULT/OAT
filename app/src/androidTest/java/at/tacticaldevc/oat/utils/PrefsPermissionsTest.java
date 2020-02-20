package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;

import at.tacticaldevc.oat.R;

import static at.tacticaldevc.oat.utils.Prefs.fetchPermissions;
import static at.tacticaldevc.oat.utils.Prefs.isPermissionGranted;
import static at.tacticaldevc.oat.utils.Prefs.savePermissions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrefsPermissionsTest {
    private static final String DOCUMENT_NAME_TEST = "oat-permissions";

    @Before
    public void init() {
        cleanup();
    }

    @Test
    public void fetchPermissionsWithData() {
        // prepare
        Map<String, Boolean> permissions = setup();

        // test
        Map<String, Boolean> result = fetchPermissions(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(permissions).isEqualTo(result);
    }

    @Test
    public void fetchPermissionsWithoutValues() {
        // test
        Map<String, Boolean> result = fetchPermissions(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void isPermissionGrantedWithInvalidValues() {
        // test
        assertThrows(IllegalArgumentException.class, () -> isPermissionGranted(null, null));
        assertThrows(IllegalArgumentException.class, () -> isPermissionGranted(InstrumentationRegistry.getInstrumentation().getTargetContext(), null));
        assertThrows(IllegalArgumentException.class, () -> isPermissionGranted(null, InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.oat_permissions_key_send_sms)));
    }

    @Test
    public void isPermissionGrantedWithoutData() {
        // test
        boolean result = isPermissionGranted(InstrumentationRegistry.getInstrumentation().getTargetContext(), InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.oat_permissions_key_receive_sms));

        // assert
        assertThat(result).isFalse();
    }

    @Test
    public void isPermissionGrantedWithData() {
        // prepare
        String permission = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.oat_permissions_key_access_fine_location);
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put(permission, true);
        Prefs.savePermissions(InstrumentationRegistry.getInstrumentation().getTargetContext(), permissions);

        // test
        boolean result = Prefs.isPermissionGranted(InstrumentationRegistry.getInstrumentation().getTargetContext(), permission);

        // assert
        assertThat(result).isTrue();
    }

    @Test
    public void savePermissionsAddNewPermission() {
        // prepare
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("SEND_SMS", true);

        // test
        Map<String, Boolean> result = savePermissions(InstrumentationRegistry.getInstrumentation().getTargetContext(), permissions);

        // assert
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, Boolean> loadedPermissions = new HashMap<>();
        for (String s : pref.getAll().keySet()) {
            loadedPermissions.put(s, pref.getBoolean(s, false));
        }

        assertThat(result).isSameAs(permissions);
        assertThat(loadedPermissions).isEqualTo(permissions);
    }

    @Test
    public void savePermissionsUpdateExistingPermission() {
        // prepare
        Map<String, Boolean> permissions = setup();
        String permissionToUpdate = "SEND_SMS";
        permissions.put(permissionToUpdate, true);

        // test
        Map<String, Boolean> result = savePermissions(InstrumentationRegistry.getInstrumentation().getTargetContext(), permissions);

        // assert
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, Boolean> loadedPermissions = new HashMap<>();
        for (String s : pref.getAll().keySet()) {
            loadedPermissions.put(s, pref.getBoolean(s, false));
        }

        assertThat(result).isSameAs(permissions);
        assertThat(loadedPermissions).isEqualTo(result);
    }

    @Test
    public void savePermissionsDeletePermission() {
        // prepare
        Map<String, Boolean> permissions = setup();
        String permissionToRemove = "ACCESS_COARSE_LOCATION";
        permissions.remove(permissionToRemove);

        // test
        Map<String, Boolean> result = savePermissions(InstrumentationRegistry.getInstrumentation().getTargetContext(), permissions);

        // assert
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        Map<String, Boolean> loadedPermissions = new HashMap<>();
        for (String s : pref.getAll().keySet()) {
            loadedPermissions.put(s, pref.getBoolean(s, false));
        }

        assertThat(result).isSameAs(permissions);
        assertThat(loadedPermissions).isEqualTo(permissions);
    }


    @AfterEach
    public void tearDown() {
        cleanup();
    }

    private void cleanup() {
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    private Map<String, Boolean> setup() {
        SharedPreferences pref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        HashMap<String, Boolean> permissions = new HashMap<>();
        permissions.put("SEND_SMS", false);
        permissions.put("RECEIVE_SMS", true);
        permissions.put("ACCESS_FINE_LOCATION", false);
        permissions.put("ACCESS_COARSE_LOCATION", true);

        for (String key : permissions.keySet()) {
            editor.putBoolean(key, permissions.get(key));
        }

        editor.apply();

        return permissions;
    }

}