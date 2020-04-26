package at.tacticaldevc.oat.ui.TrustedNumbers;

import androidx.annotation.Nullable;

import static at.tacticaldevc.oat.utils.Ensurer.ensurePhoneNumberIsValid;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;

/**
 * This class is used by the UI to display the Trusted Contacts
 */
public class Contact {
    private String name, number;

    public Contact(String name, String number) {
        ensureStringIsValid(name, name);
        ensurePhoneNumberIsValid(number, number);
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        ensureStringIsValid(name, "contact name");
        this.name = name;
    }

    public void setNumber(String number) {
        ensurePhoneNumberIsValid(number, "contact number");
        this.number = number;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Contact)) return false;
        return (name.equals(((Contact) obj).getName()) && number.equals(((Contact) obj).getNumber()));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
