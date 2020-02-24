package at.tacticaldevc.oat.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import at.tacticaldevc.oat.ui.TrustedNumbers.Contact;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;

/**
 * Provides Transformation Support for the Prefs class
 */
public class PrefsSupport {

    /**
     * Transforms the {@link Map}<String, String> returned by the {@link Prefs} class to a {@link Set}<Contact> that can be used by the UI
     *
     * @param contactMap the contact Map that should be transformed
     * @return a {@link Set}<Contact> containing the contents of the contact Map
     */
    public static Set<Contact> toContactSet(Map<String, String> contactMap) {
        ensureNotNull(contactMap, "contact Map");

        Set<Contact> result = new LinkedHashSet<>();
        for (String key : contactMap.keySet()) {
            result.add(new Contact(contactMap.get(key), key));
        }
        return result;
    }

    /**
     * Transforms the {@link Set}<Contact> used by the UI to a {@link Map}<String,String>
     *
     * @param contactSet the contact Set that schould be transformed
     * @return a {@link Map}<String,String> containing the contents of the contact Set
     */
    public static Map<String, String> toContactMap(Set<Contact> contactSet) {
        ensureNotNull(contactSet, "contact Set");

        Map<String, String> result = new HashMap<>();
        for (Contact c : contactSet) {
            result.put(c.getNumber(), c.getName());
        }
        return result;
    }
}
