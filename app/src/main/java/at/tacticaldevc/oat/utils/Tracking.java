package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.listeners.GPSListenerSMS;

import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A helper class for location tracking
 *
 * @version 0.1
 */
public class Tracking {

    /**
     * Sends the current position via SMS
     *
     * @param context         the context of the Application
     * @param phoneNumber     the phone number that should receive the reply
     * @param locationManager (optional) the Location Manager to be used. If no Location Manager is specified, a new one is fetched.
     * @throws SecurityException if the User revoked the Permission to access GPS without deactivating the feature
     */
    public static void sendCurrentCoordinatesViaSMS(Context context, String phoneNumber, LocationManager locationManager) throws SecurityException {
        if (Prefs.fetchFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_fetch_gps_position))) {
            LocationManager lManager = locationManager;
            if (locationManager == null)
                locationManager = getSystemService(context, LocationManager.class);

            // Location Listener
            GPSListenerSMS gpsListener = new GPSListenerSMS(context, phoneNumber);

            // Check Location Accuracy
            boolean fineAccuracy = Prefs.isPermissionGranted(context, context.getString(R.string.oat_permissions_key_access_fine_location));

            // Criteria
            Criteria criteria = new Criteria();
            if (fineAccuracy) criteria.setAccuracy(Criteria.ACCURACY_FINE);
            else criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

            locationManager.requestSingleUpdate(criteria, gpsListener, null);
        } else {
            SMSCom.replyErrorSMS_FeatureDisabled(context, phoneNumber, context.getString(R.string.oat_features_name_fetch_gps_position));
        }
    }
}
