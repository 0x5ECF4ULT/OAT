package at.tacticaldevc.oat.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.tacticaldevc.oat.ui.TrustedNumbers.Contact;

import static at.tacticaldevc.oat.utils.PrefsSupport.toContactMap;
import static at.tacticaldevc.oat.utils.PrefsSupport.toContactSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrefsSupportTest {

    @Test
    public void toContactSetWithInvalidValues() {
        // assert
        assertThrows(IllegalArgumentException.class, () -> toContactSet(null));
    }

    @Test
    public void toContactSetWithoutData() {
        // prepare
        Map<String, String> map = new HashMap<>();

        // test
        Set<Contact> result = toContactSet(map);

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void toContactSetWithData() {
        // prepare
        Map<String, String> map = new HashMap<>();
        map.put("+43 677 00000000", "contact 1");
        map.put("+43 677 00000001", "contact 2");

        // test
        Set<Contact> result = toContactSet(map);

        // assert
        assertThat(result).containsOnly(new Contact("contact 1", "+43 677 00000000"), new Contact("contact 2", "+43 677 00000001"));
    }

    @Test
    public void toContactMapWithInvalidValues() {
        // assert
        assertThrows(IllegalArgumentException.class, () -> toContactMap(null));
    }

    @Test
    public void toContactMapWithoutData() {
        // prepare
        Set<Contact> set = new HashSet<>();

        // test
        Map<String, String> result = toContactMap(set);

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void toContactMapWithData() {
        // prepare
        Set<Contact> set = new HashSet<>();
        set.add(new Contact("contact 1", "+43 677 00000000"));
        set.add(new Contact("contact 2", "+43 677 00000001"));

        // test
        Map<String, String> result = toContactMap(set);

        // assert
        Map<String, String> expected = new HashMap<>();
        expected.put("+43 677 00000000", "contact 1");
        expected.put("+43 677 00000001", "contact 2");
        assertThat(result).isEqualTo(expected);
    }

}
