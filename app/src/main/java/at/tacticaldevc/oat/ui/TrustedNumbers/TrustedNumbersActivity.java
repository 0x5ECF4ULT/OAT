package at.tacticaldevc.oat.ui.TrustedNumbers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Set;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Prefs;

public class TrustedNumbersActivity extends AppCompatActivity {
    private FloatingActionButton AddTrustedNumberButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_numbers);
        AddTrustedNumberButton = findViewById(R.id.add_trusted_numbers);
        AddTrustedNumberButton.setOnClickListener(cl -> openAddDialog());


        recyclerView = findViewById(R.id.TrustedNumbersList);
        recyclerView.setHasFixedSize(true);
        recyclerLayout = new LinearLayoutManager(this);
        Prefs.fetchTrustedContacts(this);
        recyclerView.setLayoutManager(recyclerLayout);
       // recyclerView.setAdapter(new NumberAdapter());

    }

    public void openAddDialog(){
        AddDialog ad = new AddDialog();
        ad.show(getSupportFragmentManager(), "Add Dialog");
    }
}
