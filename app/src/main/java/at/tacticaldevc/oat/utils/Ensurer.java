package at.tacticaldevc.oat.utils;

import android.util.Patterns;

/**
 * The Ensurer is a Helper Class that provides Methods to ensure that a value satisfies certain constraints
 */
public class Ensurer {


    /**
     * Ensures that a target Object of type T is not null
     *
     * @param obj  the Object to be checked
     * @param name the name of the Object (used by the Exception)
     * @return the unchanged Object
     */
    public static <T> T ensureNotNull(T obj, String name) {
        if (obj == null) throw new IllegalArgumentException(name + " cannot be null!");
        return obj;
    }


    /**
     * Ensures that a target String is valid
     * A String is valid if it is...
     * * ... not null
     * * ... not empty
     *
     * @param string the String to be validated
     * @param name   the name of the String (used by the Exception)
     * @return the unchanged String
     */
    public static String ensureStringIsValid(String string, String name) {
        ensureNotNull(string, name);
        if (string.trim().isEmpty()) throw new IllegalArgumentException(name + " cannot be empty!");
        return string;
    }


    /**
     * Ensures that target Phone Number is valid
     *
     * @param phoneNumber the phone number to be validated
     * @param name        the name of the phone number
     * @return the phone number as an unchanged String
     */
    public static String ensurePhoneNumberIsValid(String phoneNumber, String name) {
        ensureStringIsValid(phoneNumber, name);
        if (!Patterns.PHONE.matcher(phoneNumber).matches())
            throw new IllegalArgumentException("phoneNumber '" + name + "' is invalid! (" + phoneNumber + ")");

        /*if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches())
            throw new IllegalArgumentException("phoneNumber '" + name + "' is invalid! (" + phoneNumber + ")");*/
        return phoneNumber;
    }
}
