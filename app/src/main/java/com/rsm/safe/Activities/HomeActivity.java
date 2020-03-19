package com.rsm.safe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.rsm.safe.Adapters.TabAdapter;
import com.rsm.safe.R;
import com.rsm.safe.Constants.ConstantsFunction;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ConstantsFunction.removeFade(getWindow());
        ConstantsFunction.setTransitionDuration(getWindow());

        toolbar = findViewById(R.id.up_toolbar);
        viewPager = findViewById(R.id.h_viewPager);
        tabLayout = findViewById(R.id.h_tabLayout);

        setSupportActionBar(toolbar);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_profile:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                ConstantsFunction.startActivity(HomeActivity.this, LoginActivity.class);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
