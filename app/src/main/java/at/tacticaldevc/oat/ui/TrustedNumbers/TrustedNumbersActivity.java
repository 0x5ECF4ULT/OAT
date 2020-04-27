package at.tacticaldevc.oat.ui.TrustedNumbers;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Prefs;
import at.tacticaldevc.oat.utils.PrefsSupport;

public class TrustedNumbersActivity extends AppCompatActivity {
    private FloatingActionButton AddTrustedNumberButton;
    private RecyclerView recyclerView;
    private ContactAdapter ContactAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_numbers);

        initTrustedNumberButton();

        initRecyclerView();
    }

    public void openAddDialog(){
        AddDialog ad = new AddDialog(ContactAdapter);
        ad.show(getSupportFragmentManager(), "Add Dialog");
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.TrustedNumbersList);
        recyclerView.setHasFixedSize(true);
        recyclerLayout = new LinearLayoutManager(this);
        ContactAdapter = new ContactAdapter(PrefsSupport.toContactSet(Prefs.fetchTrustedContacts(this)));
        ContactAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(ContactAdapter);
        recyclerView.setLayoutManager(recyclerLayout);
    }

    public void initTrustedNumberButton(){
        AddTrustedNumberButton = findViewById(R.id.add_trusted_numbers);
        AddTrustedNumberButton.setOnClickListener(cl -> openAddDialog());
    }
}
