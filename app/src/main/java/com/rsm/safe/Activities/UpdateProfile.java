package com.rsm.safe.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rsm.safe.Constants.ConstantsVariables;
import com.rsm.safe.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class UpdateProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText nameText, emailText, verified;
    private ImageView profile;
    private Toolbar toolbar;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.up_name);
        emailText = findViewById(R.id.up_email);
        verified = findViewById(R.id.up_status);
        profile = findViewById(R.id.up_profileImage);
        toolbar = findViewById(R.id.up_toolbar);
        save = findViewById(R.id.up_save);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fillData();

        profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent profileIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(profileIntent, ConstantsVariables.PERMISSIONCODE);
                return true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                if (!Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()){
                    emailText.setError("Enter Valid Email");
                    flag = false;
                }

                if (nameText.getText().toString().isEmpty()){
                    nameText.setError(ConstantsVariables.ERROR);
                    flag = flag;
                }

                if (!flag)
                    return;

                UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();

                if (!firebaseAuth.getCurrentUser().getDisplayName().toLowerCase().trim().equals(nameText.getText().toString().trim().toLowerCase())){
                    builder.setDisplayName(nameText.getText().toString());
                }

                if (!firebaseAuth.getCurrentUser().getEmail().trim().toLowerCase().equals(emailText.getText().toString().trim().toLowerCase())){
                    emailText.setEnabled(false);
                    firebaseAuth.getCurrentUser().updateEmail(emailText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                emailText.setEnabled(true);
                            }
                            else {
                                emailText.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ConstantsVariables.PERMISSIONCODE)
            return;

        assert data != null;
        Bitmap image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

        assert image != null;
        uploadToDatabase(image);

    }

    private void uploadToDatabase(final Bitmap image) {
        findViewById(R.id.up_ProgressBar).setVisibility(View.VISIBLE);
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference().child("profile");
        final StorageReference mProfile = mStorageReference.child(firebaseAuth.getCurrentUser().getUid() + ".jpg");
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
        mProfile.putBytes(arrayOutputStream.toByteArray()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                mProfile.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(task.getResult()).build();
                        firebaseAuth.getCurrentUser().updateProfile(changeRequest);
                        profile.setImageBitmap(image);
                        findViewById(R.id.up_ProgressBar).setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void fillData(){
        nameText.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
        emailText.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
        if (firebaseAuth.getCurrentUser().isEmailVerified()){
            verified.setText(R.string.verified);
        }
        else {
            verified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Verification Email Set", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        Picasso.get().load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(profile, new Callback() {
            @Override
            public void onSuccess() {
                findViewById(R.id.up_ProgressBar).setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                findViewById(R.id.up_ProgressBar).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(UpdateProfile.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
