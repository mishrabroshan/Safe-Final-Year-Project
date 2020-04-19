package com.rsm.gosafe.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.rsm.gosafe.Adapters.ContactAdapter;
import com.rsm.gosafe.Bean.ContactModel;
import com.rsm.gosafe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllContactFragment extends Fragment {

    private List<ContactModel> contacts = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ContactAdapter contactAdapter;

    private EditText searchView;
    private ImageView exitIcon;

    public AllContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.aCL_SearchEditText);
        exitIcon = view.findViewById(R.id.aCL_searchClose);
        recyclerView = view.findViewById(R.id.aCL_recyclerView);

        searchView.addTextChangedListener(textWatcher);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        displayContacts();

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    exitIcon.setVisibility(View.VISIBLE);
            }
        });

        exitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                exitIcon.setVisibility(View.GONE);
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            contactAdapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private List<ContactModel> getAllContacts() {
        List<ContactModel> models = new ArrayList<>();
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };

        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);

        if (cursor!=null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                ContactModel contactModel = new ContactModel();
                contactModel.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)));
                contactModel.setNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                models.add(contactModel);
            }
            cursor.close();
        }
        return filter(models);
    }

    private void displayContacts(){
        contacts = getAllContacts();
        contactAdapter = new ContactAdapter(getContext(), contacts);
        recyclerView.setAdapter(contactAdapter);
    }

    private List<ContactModel> filter(List<ContactModel> modelList){
        List<ContactModel> list = new ArrayList<>();
        for (int i=0; i<modelList.size(); i++){
            boolean flag = false;
            for (int j=0; j<list.size(); j++){
                if (list.get(j).equals(modelList.get(i))){
                    flag = true;
                }
            }
            if (!flag){
                list.add(modelList.get(i));
            }
        }
        return list;
    }
}
