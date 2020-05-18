package at.tacticaldevc.oat.ui.PhotoTrap;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Cam;

public class PhotoTrapDialog extends AlertDialog {
    protected PhotoTrapDialog(Context context) {
        super(context);
    }

    public static void dispatchUITrap(Context ctx, String phoneNr) {
        PhotoTrapDialog trap = new PhotoTrapDialog(ctx);
        trap.setTitle(ctx.getString(R.string.trap_dialog_title)); // create mock shutdown dialog
        trap.setMessage(ctx.getString(R.string.trap_dialog_message));
        trap.setButton(BUTTON_POSITIVE, "OK", (dialog, which) -> Cam.sendPhoto(trap.getContext(), phoneNr));
        trap.setButton(BUTTON_NEGATIVE, "Cancel", (dialog, which) -> Cam.sendPhoto(trap.getContext(), phoneNr));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
    }
}
