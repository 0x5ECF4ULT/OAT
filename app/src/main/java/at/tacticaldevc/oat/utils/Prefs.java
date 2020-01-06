package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A helper class for preference management
 * OAT uses Shared Preferences to store all data that is needed.
 * This ensures that the users stay in full control of their data and no data is saved on third-party servers.
 *
 * @version 0.2
 */
public class Prefs {

    // Shared Preference information
    private final static String DOCUMENT_NAME_DATA = "oat-data";
    private final static String DOCUMENT_NAME_PERMISSIONS = "oat-permissions";
    private final static String KEY_TRUSTED_CONTACTS = "trusted-contacts";

    // Trusted Contacts

    /**
     * Returns all trusted contacts
     *
     * @param context the Context of the Application
     * @return A Set<String> with all trusted Contacts
     */
    public static Set<String> getAllTrustedContacts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_DATA, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_TRUSTED_CONTACTS, new HashSet<>());
    }

    /**
     * Adds a new trusted contact
     *
     * @param context     the Context of the Application
     * @param phoneNumber the phone number to be added
     * @return
     * @throws IllegalArgumentException if phone number is 0 or smaller
     */
    public static String addTrustedContact(Context context, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            throw new IllegalArgumentException("Invalid phone number (" + phoneNumber + ")");
        if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches())
            throw new IllegalArgumentException("Invalid phone number (" + phoneNumber + ")");

        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> numbers = getAllTrustedContacts(context);
        numbers.add(phoneNumber);

        editor.putStringSet(KEY_TRUSTED_CONTACTS, numbers);
        editor.apply();
        return phoneNumber;
    }

    /**
     * Removes an existing trusted contact
     *
     * @param context     the Context of the Application
     * @param phoneNumber the phone number to be removed
     * @return the removed Trusted Contact if removal was successful or null if it wasn't
     */
    public static String removeTrustedContact(Context context, String phoneNumber) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> phoneNumbers = getAllTrustedContacts(context);

        if (phoneNumbers.contains(phoneNumber)) {
            phoneNumbers.remove(phoneNumber);
            editor.putStringSet(KEY_TRUSTED_CONTACTS, phoneNumbers);
            editor.apply();
            return phoneNumber;
        }
        return null;
    }


    // Permissions

    /**
     * Fetches the permissions of the App
     *
     * @param context the Context of the Application
     * @return a HashMap with all permissions
     */
    public static Map<String, Boolean> fetchPermissions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_PERMISSIONS, Context.MODE_PRIVATE);

        HashMap<String, Boolean> permissions = new HashMap<>();

        Set<String> permissionKeys = prefs.getAll().keySet();
        boolean value = false;
        for (String s : permissionKeys) {
            value = prefs.getBoolean(s, false);
            permissions.put(s, value);
        }
        return permissions;
    }

    /**
     * Updates the saved permissions
     *
     * @param context     the Context of the Application
     * @param permissions the new status that should be saved
     * @return
     */
    public static Map<String, Boolean> updatePermissions(Context context, Map<String, Boolean> permissions) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_PERMISSIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Map<String, Boolean> savedPermissions = fetchPermissions(context);
        if (savedPermissions.size() > permissions.size()) {
            editor.clear();
            savedPermissions = new HashMap<>();
        }

        for (String key : permissions.keySet()) {
            if (!permissions.get(key).equals(savedPermissions.get(key)))
                editor.putBoolean(key, permissions.get(key));
        }

        editor.apply();
        return permissions;
    }


}
