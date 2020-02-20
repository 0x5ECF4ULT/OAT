package at.tacticaldevc.oat.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import at.tacticaldevc.oat.utils.SMSCom;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensurePhoneNumberIsValid;

/**
 * This Location Listener is used, to reply to the request of the current Location via SMS.
 */
public class GPSListenerSMS implements LocationListener {

    private String phoneNumber;
    private Context context;

    public GPSListenerSMS(Context context, String phoneNumber) {
        this.phoneNumber = ensurePhoneNumberIsValid(phoneNumber, "phone number");
        this.context = ensureNotNull(context, "Application Context");
    }

    /**
     * Called when the Location changed
     *
     * @param location the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        SMSCom.replyFetchGPSPosition(context, phoneNumber, location);
    }

    /**
     * @deprecated see https://developer.android.com/reference/kotlin/android/location/LocationListener for more information
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /**
     * Called when the User disables the Provider
     * Not used
     *
     * @param provider the Provider
     */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /**
     * Called when the user enables the Provider
     * Not used
     *
     * @param provider the Provider
     */
    @Override
    public void onProviderDisabled(String provider) {
    }
}
