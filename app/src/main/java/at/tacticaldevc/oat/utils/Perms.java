package at.tacticaldevc.oat.utils;

import android.Manifest;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class for permission management
 */
public class Perms {
    public static final int YAY = PackageManager.PERMISSION_GRANTED;
    public static final int BOO = PackageManager.PERMISSION_DENIED;

    /*Permission constants*/
    public static final String PERM_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String PERM_RECV_SMS = Manifest.permission.RECEIVE_SMS;

    public static final String PERM_LOC_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERM_LOC_FINE = Manifest.permission.ACCESS_FINE_LOCATION;

    private static Map<String, Integer> perms = new HashMap<>();

    public static String[] getAllPermsDefinitions() {
        return (String[]) perms.keySet().toArray();
    }

    public static void loadPermissionStates() {
        //Dictionary<String, Integer> = Preferences.loadPermissions()...
        //load into perms
    }
}
