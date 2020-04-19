package com.rsm.gosafe.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsm.gosafe.Bean.TrustedContactModel;
import com.rsm.gosafe.Database.SafeDatabase;
import com.rsm.gosafe.R;

import java.util.List;

public class TrustedContactAdapter extends RecyclerView.Adapter<TrustedContactAdapter.ViewHolder> {

    private Context context;
    private List<TrustedContactModel> contacts;

    public TrustedContactAdapter(Context context, List<TrustedContactModel> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.trusted_contact_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int ID = contacts.get(position).getID();
        String Name = contacts.get(position).getName();
        long Number = contacts.get(position).getNumber();

        holder.ID = ID;
        holder.Position = position;
        holder.ContactName.setText(Name);
        holder.ContactNumber.setText(String.valueOf(Number));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ContactName, ContactNumber;
        private int ID, Position;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ContactName = itemView.findViewById(R.id.tLL_NameTextView);
            ContactNumber = itemView.findViewById(R.id.tLL_NumberTextView);
            Button removeTrusted = itemView.findViewById(R.id.tLL_SubmitButton);

            removeTrusted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SafeDatabase safeDatabase = new SafeDatabase(context);
                    if (safeDatabase.removeTrusted(ID)){
                        contacts.remove(Position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Some Thing Went Wrong!..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
