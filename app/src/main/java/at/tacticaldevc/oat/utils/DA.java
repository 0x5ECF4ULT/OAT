package at.tacticaldevc.oat.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.listeners.DeviceAdminListener;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;

/**
 * A helper class for device administration
 */
public class DA {

    private static final int DA_REQUEST_CODE = 1234;

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
            Prefs.setLockdownStatus(ctx, true);
            pol.lockNow();
            SMSCom.replyDeviceLocked(ctx, phone);
        } else
            SMSCom.replyErrorSMS_FeatureDisabled(ctx, phone, ctx.getString(R.string.oat_features_name_trigger_lockdown));
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
            Prefs.setLockdownStatus(ctx, false);
            SMSCom.replyDeviceUnlocked(ctx, phone);
        } else
            SMSCom.replyErrorSMS_FeatureDisabled(ctx, phone, ctx.getString(R.string.oat_features_name_lift_lockdown));
    }

    public static void request_deviceAdmin(Activity ctx) {
        DevicePolicyManager dpm = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminComponent = new ComponentName(ctx, DeviceAdminListener.class);

        if (!dpm.isAdminActive(adminComponent)) {
            Intent activateDeviceAdminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);

            ctx.startActivityForResult(activateDeviceAdminIntent, DA_REQUEST_CODE);
        }
    }
}
