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
 * @version 0.1
 */
public class SMSListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            // Fetch trusted Contacts
            Set<String> contacts = Prefs.fetchTrustedContacts(context);

            for (SmsMessage msg : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                if (contacts.contains(msg.getOriginatingAddress())) {
                    String[] msgParts = msg.getMessageBody().split("\n");
                    String triggerWord = Prefs.fetchCommandTriggerWord(context);
                    if (triggerWord.equals(msgParts[0]) && Prefs.verifyApplicationPassword(context, msgParts[2])) { // Message starts with trigger word and uses the correct password
                        dispatchToFeature(context, msg.getOriginatingAddress(), msgParts[1]);
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