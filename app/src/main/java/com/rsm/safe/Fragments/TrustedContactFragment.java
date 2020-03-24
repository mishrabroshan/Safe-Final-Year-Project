package com.rsm.safe.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rsm.safe.Database.SafeDatabase;
import com.rsm.safe.R;

public class TrustedContactFragment extends Fragment {


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
        SafeDatabase safeDatabase = new SafeDatabase(view.getContext());
        if (safeDatabase.getCount() > 0){
            displayTrusted(view);
        }
    }

    private void displayTrusted(View view) {
    }
}
