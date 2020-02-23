package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;

import at.tacticaldevc.oat.R;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensurePhoneNumberIsValid;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;

/**
 * A helper class for SMS communication
 *
 * @version 0.3
 */
public class SMSCom {

    /**
     * Send a reply to let the User know, that a certain feature was not enabled
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the phone number that requested the feature to be activated
     * @param featureName the feature that could not be used, as it was deactivated
     */
    public static void replyErrorSMS_FeatureDisabled(Context context, String phoneNumber, String featureName) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "target phone number");
        ensureStringIsValid(featureName, "name of disabled feature");

        SmsManager smsManager = fetchSMSManager();
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_error_feature_disabled), featureName), null, null);
    }

    /**
     * Send a reply to let the User know, that the requested feature does not exist
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the phone number that requested the non-existent feature
     * @param featureName the name of the missing feature
     */
    public static void replyErrorSMS_FeatureNotFound(Context context, String phoneNumber, String featureName) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "phone number");
        ensureStringIsValid(featureName, "feature name");

        SmsManager smsManager = fetchSMSManager();
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_error_feature_not_found), featureName), null, null);
    }

    public static void replyErrorSMS_InvalidPassword(Context context, String phoneNumber) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "phone number");

        SmsManager smsManager = fetchSMSManager();
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_error_invalid_password)), null, null);
    }

    /**
     * Reply with the given GPS Position of the Device.
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the phone number that asked for the GPS Position of the Device
     * @param location    the current location of the Device
     */
    public static void replyFetchGPSPosition(Context context, String phoneNumber, Location location) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "target phone number");
        ensureNotNull(location, "current location");

        SmsManager smsManager = fetchSMSManager();
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_send_gps_position), location.getLatitude(), location.getLongitude()), null, null);
    }

    /**
     * @return the SMSManager associated with the default Subscription or null, if no valid subscription could be found
     */
    private static SmsManager fetchSMSManager() {
        int subscriptionID = SmsManager.getDefaultSmsSubscriptionId();
        if (!(subscriptionID == SubscriptionManager.INVALID_SUBSCRIPTION_ID)) {
            return SmsManager.getSmsManagerForSubscriptionId(subscriptionID);
        }
        // Error handling missing
        return null;
    }
}
