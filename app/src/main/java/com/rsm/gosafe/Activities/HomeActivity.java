package com.rsm.gosafe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RemoteViews;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.rsm.gosafe.Adapters.TabAdapter;
import com.rsm.gosafe.Constants.ConstantsVariables;
import com.rsm.gosafe.NotificationManager;
import com.rsm.gosafe.R;
import com.rsm.gosafe.Constants.ConstantsFunction;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ConstantsFunction.removeFade(getWindow());
        ConstantsFunction.setTransitionDuration(getWindow());

        Toolbar toolbar = findViewById(R.id.up_toolbar);
        ViewPager viewPager = findViewById(R.id.h_viewPager);
        TabLayout tabLayout = findViewById(R.id.h_tabLayout);

        setSupportActionBar(toolbar);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        createNotification();
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

    private void createNotification(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        RemoteViews panic = new RemoteViews(getPackageName(), R.layout.notificationlayout);
        Intent intent = new Intent(this, NotificationManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 5, intent, 0);
        panic.setOnClickPendingIntent(R.id.panic_button, pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, ConstantsVariables.CHANNELID).
                    setSmallIcon(R.drawable.logo).
                    setCustomContentView(panic).
                    build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            notificationManager.notify(1, notification);
        }
    }
}
