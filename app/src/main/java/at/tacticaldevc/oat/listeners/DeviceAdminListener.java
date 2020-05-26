package at.tacticaldevc.oat.listeners;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;

import androidx.annotation.NonNull;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Prefs;

public class DeviceAdminListener extends DeviceAdminReceiver {

    private static final DeviceAdminListener dal;

    static {
        dal = new DeviceAdminListener();
    }

    private DeviceAdminListener() {
    }

    public static DeviceAdminListener getDAObject() {
        return dal;
    }

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        Prefs.saveFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_trigger_lockdown), true);
        Prefs.saveFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_lift_lockdown), true);
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        Prefs.saveFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_trigger_lockdown), false);
        Prefs.saveFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_lift_lockdown), false);
    }

    @Override
    public void onPasswordSucceeded(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle user) {
        if (Prefs.getLockdownStatus(context))
            getManager(context).lockNow();
    }
}
