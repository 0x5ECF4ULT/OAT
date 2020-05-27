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
import at.tacticaldevc.oat.utils.PrefsSupport;

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
                .setTitle(R.string.trustednumber_adddialog_titel)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!number.getText().toString().isEmpty() && !name.getText().toString().isEmpty()){
                            try{
                                Ensurer.ensurePhoneNumberIsValid(number.getText().toString(), name.getText().toString());
                                Prefs.saveTrustedContact(getContext(), number.getText().toString(), name.getText().toString());
                                ca.list.clear();
                                ca.list.addAll(PrefsSupport.toContactSet(Prefs.fetchTrustedContacts(getContext())));
                            }
                            catch (IllegalArgumentException e){
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setTitle(R.string.invalid_number);
                                alert.setMessage(R.string.invalid_number_message);
                                alert.setPositiveButton(R.string.ok, null);
                                alert.show();
                            }

                        }
                        else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle(R.string.fields_empty);
                            alert.setMessage(R.string.fields_empty_message);
                            alert.setPositiveButton(R.string.ok, null);
                            alert.show();
                        }
                    }
                });
        number = view.findViewById(R.id.dialog_number_text);
        name = view.findViewById(R.id.dialog_names_text);
        return builder.create();
    }
}
