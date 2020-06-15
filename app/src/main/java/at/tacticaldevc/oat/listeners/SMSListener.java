package at.tacticaldevc.oat.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.Set;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.ui.PhotoTrap.PhotoTrapDialog;
import at.tacticaldevc.oat.utils.Cam;
import at.tacticaldevc.oat.utils.DA;
import at.tacticaldevc.oat.utils.Prefs;
import at.tacticaldevc.oat.utils.SMSCom;
import at.tacticaldevc.oat.utils.Tracking;

/**
 * Listens for incoming SMS that start with the trigger word.
 * The SMS message has to be in the following format:
 * <trigger-word> <feature> <password>
 * where trigger-word is the trigger that was configured (defaults to "oat"), feature is the feature to be activated/deactivated and password is the password that was configured
 *
 * @version 0.3
 */
public class SMSListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            // Fetch trusted Contacts (phone numbers only)
            Set<String> contacts = Prefs.fetchTrustedContacts(context).keySet();

            for (SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                if (contacts.contains(msg.getOriginatingAddress())) {
                    if (msg.getMessageBody() != null) { // ignore empty messages
                        String[] msgParts = msg.getMessageBody().trim().toLowerCase().split(" ");
                        String triggerWord = Prefs.fetchCommandTriggerWord(context);
                        if (msgParts.length == 3) { // verify that the message has the correct number of parts (triggerWord feature password)
                            if (triggerWord.equals(msgParts[0])) { // Message starts with trigger word
                                if (Prefs.verifyApplicationPassword(context, msgParts[2]))// user used the correct password
                                    dispatchToFeature(context, msg.getOriginatingAddress(), msgParts[1]);
                                else
                                    SMSCom.replyErrorSMS_InvalidPassword(context, msg.getOriginatingAddress());
                            }
                        } else {
                            if (msgParts.length > 1 && triggerWord.equals(msgParts[0]))
                                SMSCom.replyErrorSMS_MalformedCommandMessage(context, msg.getOriginatingAddress());
                        }
                    }
                }
            }
        }
    }

    private void dispatchToFeature(Context context, String phoneNumber, String feature) {
        switch (feature) {
            case "lock":
            case "lockdown":
                DA.lockdown_activate(context, phoneNumber);
                break;
            case "unlock":
                DA.lockdown_deactivate(context, phoneNumber);
                break;
            case "gps":
            case "location":
            case "position":
                if (Prefs.isPermissionGranted(context, context.getString(R.string.oat_permissions_key_access_coarse_location)) || Prefs.isPermissionGranted(context, context.getString(R.string.oat_permissions_key_access_fine_location))) {
                    if (Prefs.fetchFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_fetch_gps_position))) {
                        Tracking.sendCurrentCoordinatesViaSMS(context, phoneNumber, null);
                    } else {
                        SMSCom.replyErrorSMS_FeatureDisabled(context, phoneNumber, context.getString(R.string.oat_features_name_fetch_gps_position));
                    }
                } else
                    SMSCom.replyErrorSMS_DisabledPermission(context, phoneNumber, context.getString(R.string.oat_features_name_fetch_gps_position));
                break;
            case "instant-photo":
            case "take-photo":
                if (Prefs.isPermissionGranted(context, context.getString(R.string.oat_permissions_key_camera))) {
                    if (Prefs.fetchFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_trigger_instant_photo)))
                        Cam.sendPhoto(context, phoneNumber, false);
                    else
                        SMSCom.replyErrorSMS_FeatureDisabled(context, phoneNumber, context.getString(R.string.oat_features_name_trigger_instant_photo));
                } else
                    SMSCom.replyErrorSMS_DisabledPermission(context, phoneNumber, "take-photo");
                break;
            case "photo-trap":
                if (Prefs.isPermissionGranted(context, context.getString(R.string.oat_permissions_key_camera))) {
                    if (Prefs.fetchFeatureEnabledStatus(context, context.getString(R.string.oat_features_key_trigger_photo_trap)))
                        PhotoTrapDialog.dispatchUITrap(context, phoneNumber);
                    else
                        SMSCom.replyErrorSMS_FeatureDisabled(context, phoneNumber, context.getString(R.string.oat_features_name_trigger_photo_trap));
                } else
                    SMSCom.replyErrorSMS_DisabledPermission(context, phoneNumber, "photo-trap");
                break;
            default:
                SMSCom.replyErrorSMS_FeatureNotFound(context, phoneNumber, feature);
                break;
        }
    }
}