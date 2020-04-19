package com.rsm.gosafe.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsm.gosafe.Bean.ContactModel;
import com.rsm.gosafe.Bean.TrustedContactModel;
import com.rsm.gosafe.Database.SafeDatabase;
import com.rsm.gosafe.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<ContactModel> contacts;
    private List<ContactModel> fullContacts;

    private List<ViewHolder> viewHolders = new ArrayList<>();

    public ContactAdapter(Context context, List<ContactModel> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.fullContacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.contact_list_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ContactName = contacts.get(position).getName();
        String ContactNumber = contacts.get(position).getNumber();

        holder.ContactName.setText(ContactName);
        holder.ContactNumber.setText(ContactNumber);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ContactModel> filteredContact = new ArrayList<>();
                if (charSequence.toString().isEmpty() && charSequence.length() <= 0){
                    filteredContact = fullContacts;
                }
                else {
                    for (ContactModel contactModel : fullContacts){
                        if (contactModel.getName().toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim())){
                            filteredContact.add(contactModel);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredContact;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contacts = (List<ContactModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ContactName, ContactNumber;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ContactName = itemView.findViewById(R.id.cLL_NameTextView);
            ContactNumber = itemView.findViewById(R.id.cLL_NumberTextView);
            Button addTrusted = itemView.findViewById(R.id.cLL_SubmitButton);

            addTrusted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SafeDatabase safeDatabase = new SafeDatabase(context);
                    String name = ContactName.getText().toString();
                    long number = Long.parseLong(ContactNumber.getText().toString());
                    TrustedContactModel contact = new TrustedContactModel(0, name, number);
                    int i = safeDatabase.addTrusted(contact);
                    if (i == 0){
                        Toast.makeText(context, "Added To Trusted", Toast.LENGTH_SHORT).show();
                    }
                    else if (i == 1){
                        Toast.makeText(context, "Cannot Add More than 5.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Some Thing Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
