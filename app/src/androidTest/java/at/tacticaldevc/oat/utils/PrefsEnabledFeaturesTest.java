package at.tacticaldevc.oat.utils;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;

import static at.tacticaldevc.oat.utils.Prefs.fetchFeatureEnabledStatus;
import static at.tacticaldevc.oat.utils.Prefs.fetchFeaturesEnabled;
import static at.tacticaldevc.oat.utils.Prefs.saveFeatureEnabledStatus;
import static org.assertj.core.api.Assertions.assertThat;

public class PrefsEnabledFeaturesTest {

    private static final String DOCUMENT_NAME_TEST = "oat-enabled-features";

    @Before
    public void init() {
        cleanup();
    }

    @Test
    public void testFetchFeaturesEnabled() {
        // prepare
        Map<String, Boolean> setup = setup();

        // test
        Map<String, Boolean> result = fetchFeaturesEnabled(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEqualTo(setup);
    }

    @Test
    public void testFetchFeaturesEnabledWithoutValues() {
        // test
        Map<String, Boolean> result = fetchFeaturesEnabled(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // assert
        assertThat(result).isEmpty();
    }

    @Test
    public void testFetchFeatureEnabledStatus() {
        // prepare
        Map<String, Boolean> setup = setup();
        String testedFeatureName = "PHOTO_TRAP";

        // test
        boolean result = fetchFeatureEnabledStatus(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedFeatureName);

        // assert
        assertThat(result).isEqualTo(setup.get(testedFeatureName));
    }

    @Test
    public void testFetchFeatureEnabledStatusWithNonExistentFeature() {
        // prepare
        setup();
        String testedFeatureName = "NEW_FEATURE";

        // test
        boolean result = fetchFeatureEnabledStatus(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedFeatureName);

        // assert
        assertThat(result).isFalse();
    }

    @Test
    public void testFetchFeatureEnabledStatusWithoutValues() {
        // prepare
        String testedFeatureName = "PHOTO_TRAP";

        // test
        boolean result = fetchFeatureEnabledStatus(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedFeatureName);

        // assert
        assertThat(result).isFalse();
    }

    @Test
    public void testSaveFeatureEnabledStatusUpdateValue() {
        // prepare
        Map<String, Boolean> setup = setup();
        String testedFeatureName = "GPS_TRACKING_FINE";

        // test
        boolean result = saveFeatureEnabledStatus(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedFeatureName, true);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);

        assertThat(result).isTrue();
        assertThat(prefs.getBoolean(testedFeatureName, false)).isTrue();
    }

    @Test
    public void testSaveFeatureEnabledStatusSaveNewValue() {
        // prepare
        String testedFeatureName = "NEW_FEATURE";

        // test
        boolean result = saveFeatureEnabledStatus(InstrumentationRegistry.getInstrumentation().getTargetContext(), testedFeatureName, false);

        // assert
        SharedPreferences prefs = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences(DOCUMENT_NAME_TEST, Context.MODE_PRIVATE);

        assertThat(result).isFalse();
        assertThat(prefs.getBoolean(testedFeatureName, true)).isFalse();
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
        enabledFeatures.put("GPS_TRACKING_FINE", false);
        enabledFeatures.put("GPS_TRACKING_COARSE", true);

        for (String key : enabledFeatures.keySet()) {
            editor.putBoolean(key, enabledFeatures.get(key));
        }

        editor.apply();

        return enabledFeatures;
    }
}