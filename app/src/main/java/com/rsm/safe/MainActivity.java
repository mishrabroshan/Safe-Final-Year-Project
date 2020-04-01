package com.rsm.safe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rsm.safe.Activities.HomeActivity;
import com.rsm.safe.Activities.LoginActivity;
import com.rsm.safe.Constants.ConstantsFunction;
import com.rsm.safe.Constants.ConstantsVariables;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private ImageView imageView;

    private String[] Permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstantsFunction.setTransitionDuration(getWindow());
        ConstantsFunction.removeFade(getWindow());

        layout = findViewById(R.id.mainLayout);
        imageView = findViewById(R.id.mainLogo);

        if (hasPermission()){
            startApp();
        }
        else {
            requestPermissions();
        }

    }

    void startApp(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;

                if (!ConstantsFunction.whereToGo(getApplicationContext())) {
                    intent = new Intent(MainActivity.this, LoginActivity.class);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(layout, ConstantsVariables.SPLASHLAYOUT);
                    pairs[1] = new Pair<View, String>(imageView, ConstantsVariables.SPLASHLOGO);

                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);

                    startActivity(intent, activityOptions.toBundle());
                }
                else {
                    ConstantsFunction.startActivity(MainActivity.this, HomeActivity.class);
                }

                finish();

            }
        }, ConstantsVariables.DURATION);
    }



    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, Permissions, ConstantsVariables.PERMISSIONCODE);
    }

    private boolean hasPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Permissions != null){
            for (String permission : Permissions){
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean flag = true;
        switch (requestCode){
            case ConstantsVariables.PERMISSIONCODE:
                if (grantResults.length > 0){
                    for (int i : grantResults){
                        if (i != PackageManager.PERMISSION_GRANTED){
                            flag = false;
                        }
                    }
                }
        }

        if (flag){
            startApp();
        }
        else {
            requestPermissions();
        }
    }
}
