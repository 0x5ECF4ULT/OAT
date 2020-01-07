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
    private final static String DOCUMENT_NAME_ENABLED_FEATURES = "oat-enabled-features";
    private final static String DOCUMENT_NAME_ACCEPTED_CONDITIONS = "oat-accepted-conditions";
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
        for (String s : permissionKeys) {
            permissions.put(s, prefs.getBoolean(s, false));
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
    public static Map<String, Boolean> savePermissions(Context context, Map<String, Boolean> permissions) {
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


    // Enabled Features

    /**
     * Fetches the saved enabled Features of the App
     *
     * @param context the Context of the Application
     * @return a HashMap with the enabled Status of all Features
     */
    public static Map<String, Boolean> fetchFeaturesEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_ENABLED_FEATURES, Context.MODE_PRIVATE);

        HashMap<String, Boolean> enabledFeatures = new HashMap<>();

        Set<String> featureKeys = prefs.getAll().keySet();
        for (String s : featureKeys) {
            enabledFeatures.put(s, prefs.getBoolean(s, false));
        }
        return enabledFeatures;
    }

    /**
     * Fetches if target Feature is set to enabled
     *
     * @param context the Context of the Application
     * @param key     the key of the target Feature
     * @return if the Feature is set to enabled, false if the feature could not be found
     */
    public static boolean fetchFeatureEnabledStatus(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_ENABLED_FEATURES, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    /**
     * Saves the enabled Status of target Feature
     *
     * @param context  the Context of the Application
     * @param key      the key of the target Feature whose enabled status should be saved
     * @param newValue the new value of the Feature's enabled status
     * @return the current enabled status of the Feature
     */
    public static boolean saveFeatureEnabledStatus(Context context, String key, boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_ENABLED_FEATURES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, newValue);
        editor.apply();
        return newValue;
    }

    // Accepted Terms & Conditions

    /**
     * Fetches the saved accepted Conditions
     *
     * @param context the Context of the Application
     * @return a HashMap with all conditions
     */
    public static Map<String, Boolean> fetchConditionsAccepted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_ACCEPTED_CONDITIONS, Context.MODE_PRIVATE);

        HashMap<String, Boolean> conditionsAccepted = new HashMap<>();

        Set<String> conditionKeys = prefs.getAll().keySet();
        for (String s : conditionKeys) {
            conditionsAccepted.put(s, prefs.getBoolean(s, false));
        }
        return conditionsAccepted;
    }

    /**
     * Fetches if target Condition was accepted
     *
     * @param context the Context of the Application
     * @param key     the key (name) of the target Condition
     * @return if the Condition was accepted, false if the feature could not be found
     */
    public static boolean fetchConditionAccepted(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_ACCEPTED_CONDITIONS, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    /**
     * Saves the condition accepted status ot target condition
     *
     * @param context  the Context of the Application
     * @param key      the key (name) of the target condition that should be saved
     * @param newValue the new value of the Condition's accepted status
     * @return the current accepted status of the Condition
     */
    public static boolean saveConditionAccepted(Context context, String key, boolean newValue) {
        SharedPreferences prefs = context.getSharedPreferences(DOCUMENT_NAME_ACCEPTED_CONDITIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, newValue);
        editor.apply();
        return newValue;
    }

}
