package at.tacticaldevc.oat.ui.TrustedNumbers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Prefs;
import at.tacticaldevc.oat.utils.PrefsSupport;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactItem>{

    public List<Contact> list = new ArrayList<Contact>();

    public ContactAdapter(Set<Contact> contactSet){
        list.addAll(contactSet);
    }

    @NonNull
    @Override
    public ContactItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false );
        return new ContactItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItem ni, int position) {
        Log.d("RecyclerView Item", "onBindViewHolder: called");
        Contact c = list.get(position);
        ni.name.setText(c.getName());
        ni.number.setText(c.getNumber());
        ni.c = c;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContactItem extends RecyclerView.ViewHolder implements View.OnClickListener{

        public Contact c;
        public TextView name, number;

        public ContactItem(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Delete entry?")
                    .setMessage("Do you want to delete this entry?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int ind = 0;
                            for(Contact c: list){
                                if(c.getNumber() == number.getText().toString()) {
                                    ind = list.indexOf(c);
                                    list.remove(ind);
                                }
                            }
                            Prefs.deleteTrustedContact(view.getContext(), number.getText().toString());
                            notifyItemRemoved(ind);
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        }
    }
}
