package at.tacticaldevc.oat.utils;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.exceptions.OATApplicationException;

import static at.tacticaldevc.oat.utils.Ensurer.ensureNotNull;
import static at.tacticaldevc.oat.utils.Ensurer.ensurePhoneNumberIsValid;
import static at.tacticaldevc.oat.utils.Ensurer.ensureStringIsValid;

/**
 * A helper class for SMS communication
 *
 * @version 0.4
 */
public class SMSCom {

    /**
     * Reply that the feature is disabled
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
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_error_feature_disabled), featureName), null, null);
    }

    /**
     * Reply that the message was malformed
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the phone number that send the message
     */
    public static void replyErrorSMS_MalformedCommandMessage(Context context, String phoneNumber) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "target phone number");

        SmsManager smsManager = fetchSMSManager();
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, context.getString(R.string.oat_sms_message_error_malformed_message), null, null);
    }

    /**
     * Reply that the requested feature does not exist
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
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_error_feature_not_found), featureName), null, null);
    }

    /**
     * Reply that an invalid password was provided
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the phone number to send the reply to
     */
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
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, String.format(context.getString(R.string.oat_sms_message_send_gps_position), location.getLatitude(), location.getLongitude()), null, null);
    }

    /**
     * Reply with a confirmation, that the device is now under lockdown
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the target phone number
     */
    public static void replyDeviceLocked(Context context, String phoneNumber) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "target phone number");

        SmsManager smsManager = fetchSMSManager();
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, context.getString(R.string.oat_sms_message_send_lockdown_triggered), null, null);
    }

    /**
     * Reply with a confirmation, that the device is now unlocked
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the target phone number
     */
    public static void replyDeviceUnlocked(Context context, String phoneNumber) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "target phone number");

        SmsManager smsManager = fetchSMSManager();
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, context.getString(R.string.oat_sms_message_send_lockdown_lifted), null, null);
    }

    /**
     * Send the picture that was taken
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the target phone number
     * @param imageUri    the {@link Uri} of the picture to be sent
     */
    public static void replyPhotoTaken(Context context, String phoneNumber, Uri imageUri) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "phoneNumber");
        ensureNotNull(imageUri, "the URI of the image taken by the photo trap");

        SmsManager smsManager = fetchSMSManager();
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, context.getString(R.string.oat_sms_message_send_photo_taken), null, null);
        smsManager.sendMultimediaMessage(context, imageUri, phoneNumber, null, null);
    }

    /**
     * Send the picture that was taken
     *
     * @param context     the{@link Context} of the Application
     * @param phoneNumber the target phone number
     * @param imageUri    the {@link Uri} of the picture to be sent
     */
    public static void replyPhotoTrapTriggered(Context context, String phoneNumber, Uri imageUri) {
        ensureNotNull(context, "Application Context");
        ensurePhoneNumberIsValid(phoneNumber, "phoneNumber");
        ensureNotNull(imageUri, "the URI of the image taken by the photo trap");

        SmsManager smsManager = fetchSMSManager();
        sendDeletePasswordHint(context, phoneNumber, smsManager);
        smsManager.sendTextMessage(phoneNumber, null, context.getString(R.string.oat_sms_message_send_photo_trap_triggered), null, null);
        smsManager.sendMultimediaMessage(context, imageUri, phoneNumber, null, null);
    }

    /**
     * @return the SMSManager associated with the default Subscription or null, if no valid subscription could be found
     */
    private static SmsManager fetchSMSManager() {
        int subscriptionID = SmsManager.getDefaultSmsSubscriptionId();
        if (!(subscriptionID == SubscriptionManager.INVALID_SUBSCRIPTION_ID)) {
            return SmsManager.getSmsManagerForSubscriptionId(subscriptionID);
        }
        throw OATApplicationException.forNoSMSSubscriptionFound();
    }

    /**
     * Sends the default Message to remind the users that they should delete the message containing their password
     *
     * @param context     the {@link Context} of the Application
     * @param phoneNumber the target phone number
     * @param smsManager  the {@link SmsManager} to be used
     */
    private static void sendDeletePasswordHint(Context context, String phoneNumber, SmsManager smsManager) {
        smsManager.sendTextMessage(phoneNumber, null, context.getString(R.string.oat_sms_message_hint_delete_password), null, null);
    }
}
