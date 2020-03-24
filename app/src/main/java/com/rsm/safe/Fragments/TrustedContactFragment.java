package com.rsm.safe.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rsm.safe.Adapters.TrustedContactAdapter;
import com.rsm.safe.Bean.TrustedContactModel;
import com.rsm.safe.Database.SafeDatabase;
import com.rsm.safe.R;

import java.util.List;

public class TrustedContactFragment extends Fragment {

    private SafeDatabase safeDatabase;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TrustedContactAdapter trustedContactAdapter;

    Button Refresh;

    public TrustedContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trusted_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.tCL_recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        Refresh = view.findViewById(R.id.tCL_refresh);

        safeDatabase = new SafeDatabase(view.getContext());
        if (safeDatabase.getCount() > 0){
            final View temp = view;
            Refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayTrusted(temp);
                }
            });
            displayTrusted(temp);
        }

    }

    private void displayTrusted(View view) {
        List<TrustedContactModel> list = safeDatabase.getTrustedContacts();
        if (list.size() > 0){
            trustedContactAdapter = new TrustedContactAdapter(view.getContext(), list);
            recyclerView.setAdapter(trustedContactAdapter);
        }
    }
}
