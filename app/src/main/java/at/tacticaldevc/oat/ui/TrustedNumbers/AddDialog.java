package at.tacticaldevc.oat.ui.TrustedNumbers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Ensurer;
import at.tacticaldevc.oat.utils.Prefs;

public class AddDialog extends AppCompatDialogFragment {
    private EditText number;
    private EditText name;
    private ContactAdapter ca;

    public AddDialog(ContactAdapter contactAdapter){
        ca = contactAdapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);

        builder.setView(view)
                .setTitle("Add Trusted Number")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Ensurer.ensurePhoneNumberIsValid(number.getText().toString(), name.getText().toString()).equals(number.getText().toString())){
                            Prefs.saveTrustedContact(getContext(), number.getText().toString(), name.getText().toString());
                        }
                        else {

                        }
                    }
                });
        number = view.findViewById(R.id.dialog_number_text);
        name = view.findViewById(R.id.dialog_names_text);
        return builder.create();
    }
}
