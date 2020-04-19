package com.rsm.safe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rsm.safe.R;
import com.rsm.safe.Constants.ConstantsFunction;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_avtivity);

        ConstantsFunction.setTransitionDuration(getWindow());
        ConstantsFunction.removeFade(getWindow());

        Toolbar toolbar = findViewById(R.id.up_toolbar);
        ImageView profile = findViewById(R.id.profileImage);
        Button upProfile = findViewById(R.id.profileUpdateAccount);
        Button signOut = findViewById(R.id.profileSignOut);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Picasso.get().load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).into(profile, new Callback() {
            @Override
            public void onSuccess() {
                findViewById(R.id.profileProgressBar).setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                findViewById(R.id.profileProgressBar).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Some thing went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        upProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfile.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                ConstantsFunction.startActivity(ProfileActivity.this, LoginActivity.class);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
