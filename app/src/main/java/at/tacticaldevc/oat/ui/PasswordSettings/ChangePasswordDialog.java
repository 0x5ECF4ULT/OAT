package at.tacticaldevc.oat.ui.PasswordSettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.exceptions.OATApplicationException;
import at.tacticaldevc.oat.exceptions.OATApplicationExceptionType;
import at.tacticaldevc.oat.utils.Ensurer;
import at.tacticaldevc.oat.utils.Prefs;

public class ChangePasswordDialog extends AppCompatDialogFragment {
    private EditText new_pw;
    private EditText old_pw;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_dialog, null);

        old_pw = view.findViewById(R.id.old_password_dialog_pw);
        new_pw = view.findViewById(R.id.new_password_dialog_pw);

        builder.setView(view)
                .setTitle(R.string.change_pw)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!new_pw.getText().toString().isEmpty() && !old_pw.getText().toString().isEmpty()){
                            try{
                                Prefs.savePassword(getContext(), new_pw.getText().toString(), old_pw.getText().toString());
                            }
                            catch (OATApplicationException e){
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle(R.string.fields_wrong);
                                if(e.getExceptionType().equals(OATApplicationExceptionType.PasswordMismatch)){
                                    alert.setMessage(R.string.missmatching_pws);
                                }
                                else {
                                    alert.setMessage(R.string.internal_error);
                                }
                                alert.setPositiveButton(R.string.ok, null);
                                AlertDialog alertDialog = alert.create();
                                alertDialog.show();
                            }
                        }
                        else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle(R.string.fields_empty);
                            alert.setMessage(R.string.fields_empty_message);
                            alert.setPositiveButton(R.string.ok, null);
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        }
                    }
                });


        return builder.create();
    }
}
