package com.rsm.safe.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
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
import com.rsm.safe.R;
import com.rsm.safe.Constants.ConstantsFunction;
import com.rsm.safe.Constants.ConstantsVariables;

public class LoginActivity extends AppCompatActivity {

    private TextView appname;
    private LinearLayout signUp;
    private EditText email, password;
    private Button signIn;
    private CheckBox passwordCheckBox;
    private RelativeLayout layout;
    private ImageView logo;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ConstantsFunction.setTransitionDuration(getWindow());
        ConstantsFunction.removeFade(getWindow());

        firebaseAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.al_signUpText);
        appname = findViewById(R.id.al_appName);
        logo = findViewById(R.id.logo);
        layout = findViewById(R.id.al_rl2);
        email = findViewById(R.id.al_emailText);
        password = findViewById(R.id.al_passwordText);
        signIn = findViewById(R.id.al_loginButton);
        passwordCheckBox = findViewById(R.id.al_showCheck);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(logo, ConstantsVariables.SPLASHLOGO);
                pairs[1] = new Pair<View, String>(appname, ConstantsVariables.APPNAME);
                pairs[2] = new Pair<View, String>(layout, ConstantsVariables.SPLASHLAYOUT);
                pairs[3] = new Pair<View, String>(email, ConstantsVariables.EMAILEDITTEXT);
                pairs[4] = new Pair<View, String>(password, ConstantsVariables.PASSWORDEDITTEXT);
                pairs[5] = new Pair<View, String>(signIn, ConstantsVariables.SIGNINBUTTON);
                pairs[6] = new Pair<View, String>(passwordCheckBox, ConstantsVariables.PASSWORDCHECK);

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                startActivity(intent, activityOptions.toBundle());
            }
        });

        passwordCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordCheckBox.isChecked()){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allow = true;
                if (email.getText().toString().isEmpty()){
                    email.setError(ConstantsVariables.ERROR);
                    allow = false;
                }

                if (password.getText().toString().isEmpty()){
                    password.setError(ConstantsVariables.ERROR);
                    allow = false;
                }

                if (allow){
                    signInUser(email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void signInUser(String Email, String Password) {
        firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (!ConstantsFunction.whereToGo()){
                        startActivity(new Intent(LoginActivity.this, UpdateProfile.class));
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
