package at.tacticaldevc.oat.ui.TrustedNumbers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.tacticaldevc.oat.R;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberItem>{

    private List<Number> list;

    public NumberAdapter(List<Number> numberList){
        list = numberList;
    }

    @NonNull
    @Override
    public NumberItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false );
        return new NumberItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberItem ni, int position) {
        Number n = list.get(position);
        ni.name.setText(n.name);
        ni.number.setText(n.number);
        ni.n = n;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NumberItem extends RecyclerView.ViewHolder implements View.OnClickListener{

        public Number n;
        public TextView name, number;

        public NumberItem(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Item Clicked!", Toast.LENGTH_LONG).show();
        }
    }
}
