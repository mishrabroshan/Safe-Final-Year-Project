package com.rsm.gosafe.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.rsm.gosafe.R;
import com.rsm.gosafe.Constants.ConstantsFunction;
import com.rsm.gosafe.Constants.ConstantsVariables;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextView appName;
    private ImageView logo;
    private RelativeLayout layout;
    private EditText name, email, password, confirmPassword;
    private CheckBox showPassword;
    private Button signUp;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ConstantsFunction.setTransitionDuration(getWindow());
        ConstantsFunction.removeFade(getWindow());

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        firebaseAuth = FirebaseAuth.getInstance();

        LinearLayout signtext = findViewById(R.id.as_signInText);
        appName = findViewById(R.id.as_appName);
        logo = findViewById(R.id.logo);
        layout = findViewById(R.id.as_rl2);
        name = findViewById(R.id.as_nameText);
        email = findViewById(R.id.as_emailText);
        password = findViewById(R.id.as_passwordText);
        confirmPassword = findViewById(R.id.as_confirmPasswordText);
        showPassword = findViewById(R.id.as_showCheck);
        signUp = findViewById(R.id.as_SignUpButton);

        signtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(logo, ConstantsVariables.SPLASHLOGO);
                pairs[1] = new Pair<View, String>(appName, ConstantsVariables.APPNAME);
                pairs[2] = new Pair<View, String>(layout, ConstantsVariables.SPLASHLAYOUT);
                pairs[3] = new Pair<View, String>(name, ConstantsVariables.EMAILEDITTEXT);
                pairs[4] = new Pair<View, String>(confirmPassword, ConstantsVariables.PASSWORDEDITTEXT);
                pairs[5] = new Pair<View, String>(signUp, ConstantsVariables.SIGNINBUTTON);
                pairs[6] = new Pair<View, String>(showPassword, ConstantsVariables.PASSWORDCHECK);

                ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPassword.isChecked()){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allow = true;
                if (name.getText().toString().isEmpty()){
                    name.setError(ConstantsVariables.ERROR);
                    allow = false;
                }

                if (email.getText().toString().isEmpty()){
                    email.setError(ConstantsVariables.ERROR);
                    allow = false;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
                    email.setError("Enter Valid Email Address");
                    allow = false;
                }

                if (password.getText().toString().isEmpty()){
                    password.setError(ConstantsVariables.ERROR);
                    allow = false;
                }

                if (confirmPassword.getText().toString().isEmpty()){
                    confirmPassword.setError(ConstantsVariables.ERROR);
                    allow = false;
                }

                if (!confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())){
                    Toast.makeText(SignUpActivity.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                    allow = false;
                }

                if (allow){
                    findViewById(R.id.as_progressbar).setVisibility(View.VISIBLE);
                    createUser(email.getText().toString().trim(), password.getText().toString().trim());
                }
            }
        });
    }

    private void createUser(String Email, String Password) {
        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                findViewById(R.id.as_progressbar).setVisibility(View.GONE);
                if (task.isSuccessful()){
                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().
                            setDisplayName(name.getText().toString()).
                            build();

                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(changeRequest);

                    Toast.makeText(SignUpActivity.this,"Account Created Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
