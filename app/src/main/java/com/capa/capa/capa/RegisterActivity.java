package com.capa.capa.capa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;
    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextComfirmPw;
    private EditText editTextDOB;
    private EditText editTextCarName;
    private ImageView profileImage;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    String profileImageUrl;



    Uri uriProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        buttonRegister =(Button) findViewById(R.id.registerBtn);
        editTextEmail =(EditText) findViewById(R.id.registerEmail);
        editTextPassword=(EditText) findViewById(R.id.registerPw);
        editTextName = (EditText) findViewById(R.id.registerName);
        editTextComfirmPw = (EditText) findViewById(R.id.registerConfromPw);
        editTextDOB = (EditText) findViewById(R.id.registerDOB);
        editTextCarName = (EditText) findViewById(R.id.registerCarName);

        profileImage = (ImageView) findViewById(R.id.imageView);


        buttonRegister.setOnClickListener(this);

        profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        getSupportActionBar().hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);

                profileImage.setImageBitmap(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage() {

        StorageReference profileIamgeRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage != null){
            profileIamgeRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String userName = editTextName.getText().toString().trim();
        String comfirmedPw = editTextComfirmPw.getText().toString().trim();
        String DOB = editTextDOB.getText().toString().trim();
        String carName = editTextCarName.getText().toString().trim();




        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(comfirmedPw) || TextUtils.isEmpty(DOB) || TextUtils.isEmpty(carName)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }


        if(!TextUtils.isEmpty(carName) && !TextUtils.isEmpty(DOB) && !TextUtils.isEmpty(comfirmedPw) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            progressDialog.setMessage("Registing User...");
            progressDialog.show();
            uploadImageToFirebaseStorage();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        login();
                        saveUserInformation();

                    } else {
                        Toast.makeText(RegisterActivity.this, "register unsuccessfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void saveUserInformation() {

        String displayName = editTextName.getText().toString();
        String email = editTextEmail.getText().toString().trim();

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();


        if(user != null && profileImageUrl != null) {
            try {
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .setPhotoUri(Uri.parse(profileImageUrl))
                        .build();

                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }else {
                            Toast.makeText(RegisterActivity.this, "register unsuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void storeInformation(){

        String displayName = editTextName.getText().toString().trim();
        String userEmail = editTextEmail.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String carName = editTextCarName.getText().toString().trim();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user !=null){






        }



    }

    private void login(){
        String userEmail = editTextEmail.getText().toString().trim();
        String pw = editTextPassword.getText().toString().trim();


        firebaseAuth.signInWithEmailAndPassword(userEmail,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){

                    finish();

                    Toast.makeText(RegisterActivity.this, "register Successfully", Toast.LENGTH_SHORT).show();

                    Intent homePage = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(homePage);



                }else {
                    Toast.makeText(RegisterActivity.this,"failed",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            registerUser();
        }

    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"), CHOOSE_IMAGE);
    }
    
    


}
