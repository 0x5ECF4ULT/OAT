package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;

import static at.tacticaldevc.oat.utils.Prefs.fetchPermissions;
import static at.tacticaldevc.oat.utils.Prefs.savePermissions;
import static org.assertj.core.api.Assertions.assertThat;

public class PrefsPermissionsTest {
    private static final String DOCUMENT_NAME_TEST = "oat-permissions";

    @Before
    public void init() {
        cleanup();
    }

    @Test
    public void testFetchPermissions() {
        // prepare
        Map<String, Boolean> permissions = setup();

        // test
        Map<String, Boolean> result = fetchPermissions(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(permissions).isEqualTo(result);
    }

    @Test
    public void testFetchPermissionsWithoutValues() {
        // test
        Map<String, Boolean> result = fetchPermissions(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void testSavePermissionsAddNewPermission() {
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
    public void testSavePermissionsUpdateExistingPermission() {
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
    public void testSavePermissionsDeletePermission() {
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