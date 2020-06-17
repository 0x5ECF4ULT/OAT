package at.tacticaldevc.oat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Map;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;

/**
 * A helper class for permission management
 */
public class Perms {

    /*Permission constants*/
    private static final String[] perms = {
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int reqCode = -1234;

    private static Map<String, Boolean> permsStore;

    /**
     * Method used for getting all permissions used. May get relevant sometime but now just a... method?
     *
     * @return the string representations of the permissions requested.
     */
    public static String[] getAllPermsDefinitions() {
        return perms;
    }

    /**
     * a method to load the permission states from the {@link Prefs} class. This method should be called when instantiating OAT.
     *
     * @param context is required for the {@link Prefs} class to fetch the {@link android.content.SharedPreferences} storage.
     */
    public static void loadPermissionStates(Context context) {
        ensureNotNull(context, "Context");
        permsStore = Prefs.fetchPermissions(context);
    }

    /**
     * a method to request all the permissions in {@link this.perms} using a library called Dexter.
     *
     * @param activity is required by Dexter to construct and invoke the request object.
     * @param context  is required for {@link Prefs} to save the permissions.
     * @throws IllegalArgumentException in case of a null value in the parameters or the local {@link this.permsStore}
     */
    public static void requestAllPermissions(Activity activity, Context context) {
        ensureNotNull(activity, "Activity");
        ensureNotNull(permsStore, "Permission Map");
        ensureNotNull(context, "Context");

        Dexter.withActivity(activity)
                .withPermissions(perms)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        for (PermissionGrantedResponse perm : report.getGrantedPermissionResponses()) {
                            permsStore.put(perm.getPermissionName(), true);
                        }
                        for (PermissionDeniedResponse perm : report.getDeniedPermissionResponses()) {
                            permsStore.put(perm.getPermissionName(), false);
                        }
                        Prefs.savePermissions(context, permsStore);

                        if (report.isAnyPermissionPermanentlyDenied() || report.getDeniedPermissionResponses() != null)
                            ; //TODO: Show some dialog explaining why we need that permissions
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        //TODO: again... This dialog thing is still needed
                    }
                })
                .check();
        DA.request_deviceAdmin(activity);
    }

    /**
     * a method to request the permission given using a library called Dexter.
     *
     * @param activity is required by Dexter to construct and invoke the request object.
     * @param context  is required for {@link Prefs} to save the permissions.
     * @throws IllegalArgumentException in case of a null value in the parameters or the local {@link this.permsStore}
     */
    public static void requestPermission(Activity activity, Context context, String perm) {
        ensureNotNull(activity, "Activity");
        ensureNotNull(permsStore, "Permission map");
        ensureStringIsValid(perm, "Permission");
        ensureNotNull(context, "Context");

        Dexter.withActivity(activity)
                .withPermission(perm)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permsStore.put(response.getPermissionName(), true);
                        Prefs.savePermissions(context, permsStore);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        permsStore.put(response.getPermissionName(), false);
                        Prefs.savePermissions(context, permsStore);
                        //TODO: again... This dialog thing is still needed
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                        //TODO: again... This dialog thing is still needed
                    }
                })
                .check();
    }

    public static boolean getGranted(String perm) {
        ensureNotNull(permsStore, "Permission map");
        ensureStringIsValid(perm, "Permission name");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) //getOrDefault() only one API version away .-.
            return permsStore.getOrDefault(perm, false);
        else
            return permsStore.containsKey(perm) ? permsStore.get(perm) : false;
    }
}
