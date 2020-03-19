package com.rsm.safe.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rsm.safe.Fragments.AllContactFragment;
import com.rsm.safe.Fragments.CurrentLocationFragment;
import com.rsm.safe.Fragments.TrustedContactFragment;

public class TabAdapter extends FragmentPagerAdapter {


    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new CurrentLocationFragment();
            case 1:
                return new AllContactFragment();
            case 2:
                return new TrustedContactFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "CURRENT LOCATION";
            case 1:
                return "ALL CONTACTS";
            case 2:
                return "TRUSTED CONTACTS";
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
