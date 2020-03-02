package at.tacticaldevc.oat.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.Set;

import at.tacticaldevc.oat.utils.Prefs;
import at.tacticaldevc.oat.utils.SMSCom;
import at.tacticaldevc.oat.utils.Tracking;

/**
 * Listens for incoming SMS that start with the trigger word.
 * The SMS message has to be in the following format:
 * <trigger-word> <feature> <password>
 * where trigger-word is the trigger that was configured (defaults to "oat"), feature is the feature to be activated/deactivated and password is the password that was configured
 *
 * @version 0.2
 */
public class SMSListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            // Fetch trusted Contacts (phone numbers only)
            Set<String> contacts = Prefs.fetchTrustedContacts(context).keySet();

            for (SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                if (contacts.contains(msg.getOriginatingAddress())) {
                    String[] msgParts = msg.getMessageBody().trim().toLowerCase().split(" ");
                    String triggerWord = Prefs.fetchCommandTriggerWord(context);
                    if (triggerWord.equals(msgParts[0])) { // Message starts with trigger word
                        if (Prefs.verifyApplicationPassword(context, msgParts[2]))// user used the correct password
                            dispatchToFeature(context, msg.getOriginatingAddress(), msgParts[1]);
                        else
                            SMSCom.replyErrorSMS_InvalidPassword(context, msg.getOriginatingAddress());
                    }
                }
            }
        }
    }

    private void dispatchToFeature(Context context, String phoneNumber, String feature) {
        switch (feature) {
            case "lockdown":
                // dispatch to lockdown
                break;
            case "unlock":
                // dispatch to unlock
                break;
            case "position":
                Tracking.sendCurrentCoordinatesViaSMS(context, phoneNumber, null);
                break;
            case "photo-trap":
                // dispatch to photo trap
                break;
            default:
                SMSCom.replyErrorSMS_FeatureNotFound(context, phoneNumber, feature);
                break;
        }
    }
}