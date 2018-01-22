package com.capa.capa.capa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signInButton;
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signInButton = (Button) findViewById(R.id.loginBtn);
        editTextEmail = (EditText) findViewById(R.id.userEmail);
        editTextPassword = (EditText) findViewById(R.id.userPw);

        signInButton.setOnClickListener(this);



    }
    public void registerPage(View view){

        Intent registerIntent = new Intent(this, RegisterActivity.class);

        startActivity(registerIntent);
        return;
    }
    private void logIn(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            progressDialog.setMessage("Logging In");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressDialog.dismiss();
                    if(task.isSuccessful()){

                        finish();


                        Intent homePageIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homePageIntent);
                    }else {
                        Toast.makeText(MainActivity.this,"Logging in failed",Toast.LENGTH_SHORT).show();

                    }

                }
            });



        }else{
            Toast.makeText(this,"Please enter your password or email!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == signInButton){
            logIn();
        }
    }
}
