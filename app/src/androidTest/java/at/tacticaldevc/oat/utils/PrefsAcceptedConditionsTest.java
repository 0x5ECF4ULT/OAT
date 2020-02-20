package at.tacticaldevc.oat.utils;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;

import static at.tacticaldevc.oat.utils.Prefs.fetchConditionAccepted;
import static at.tacticaldevc.oat.utils.Prefs.fetchConditionsAccepted;
import static at.tacticaldevc.oat.utils.Prefs.saveConditionAccepted;
import static org.assertj.core.api.Assertions.assertThat;

public class PrefsAcceptedConditionsTest {

    private static final String DOCUMENT_NAME_TEST = "oat-accepted-conditions";

    @Before
    public void init() {
        cleanup();
    }

    @Test
    public void fetchConditionsAcceptedWithData() {
        // prepare
        Map<String, Boolean> setup = setup();

        // test
        Map<String, Boolean> result = fetchConditionsAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEqualTo(setup);
    }

    @Test
    public void fetchConditionWithoutValues() {
        // test
        Map<String, Boolean> result = fetchConditionsAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void fetchConditionAcceptedWithData() {
        // prepare
        Map<String, Boolean> setup = setup();
        String testedConditionName = "PHOTO_TRAP";

        // test
        boolean result = fetchConditionAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedConditionName);

        // assert
        assertThat(result).isEqualTo(setup.get(testedConditionName));
    }

    @Test
    public void fetchConditionAcceptedWithNonExistentCondition() {
        // prepare
        setup();
        String testedCondition = "NEW_CONDITION";

        // test
        boolean result = fetchConditionAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedCondition);

        // assert
        assertThat(result).isFalse();
    }

    @Test
    public void fetchConditionAcceptedWithoutValues() {
        // prepare
        String testedCondition = "PHOTO_TRAP";

        // test
        boolean result = fetchConditionAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedCondition);

        // assert
        assertThat(result).isFalse();
    }

    @Test
    public void saveConditionAcceptedUpdateValue() {
        // prepare
        Map<String, Boolean> setup = setup();
        String testedCondition = "PHOTO_TRAP";

        // test
        boolean result = saveConditionAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedCondition, true);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);

        assertThat(result).isTrue();
        assertThat(prefs.getBoolean(testedCondition, false)).isTrue();
    }

    @Test
    public void saveConditionAcceptedSaveNewValue() {
        // prepare
        String testedCondition = "NEW_CONDITION";

        // test
        boolean result = saveConditionAccepted(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedCondition, true);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);

        assertThat(result).isTrue();
        assertThat(prefs.getBoolean(testedCondition, true)).isTrue();
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

        HashMap<String, Boolean> enabledFeatures = new HashMap<>();
        enabledFeatures.put("PHOTO_TRAP", false);
        enabledFeatures.put("GPS_TRACKING", true);

        for (String key : enabledFeatures.keySet()) {
            editor.putBoolean(key, enabledFeatures.get(key));
        }

        editor.apply();

        return enabledFeatures;
    }

}