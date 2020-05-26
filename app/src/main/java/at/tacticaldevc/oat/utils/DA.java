package at.tacticaldevc.oat.utils;

import android.app.admin.DevicePolicyManager;
import android.content.Context;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.listeners.DeviceAdminListener;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;

/**
 * A helper class for device administration
 */
public class DA {

    /**
     * Activate the lockdown using DA
     *
     * @param ctx   The application context
     * @param phone A valid phone number in String representation
     */
    public static void lockdown_activate(Context ctx, String phone) {
        ensureNotNull(ctx, "Context");
        ensureStringIsValid(phone, "Phone number");

        if (Prefs.fetchFeatureEnabledStatus(ctx, ctx.getString(R.string.oat_features_key_trigger_lockdown))) {
            DevicePolicyManager pol = DeviceAdminListener.getDAObject().getManager(ctx);
            pol.lockNow();
        }
    }

    /**
     * Deactivate the lockdown using DA
     *
     * @param ctx   The application context
     * @param phone A valid phone number in String representation
     */
    public static void lockdown_deactivate(Context ctx, String phone) {
        ensureNotNull(ctx, "Context");
        ensureStringIsValid(phone, "Phone number");

        if (Prefs.fetchFeatureEnabledStatus(ctx, ctx.getString(R.string.oat_features_key_trigger_lockdown))) {
            DevicePolicyManager pol = DeviceAdminListener.getDAObject().getManager(ctx);
            pol.lockNow();
        }
    }
}
