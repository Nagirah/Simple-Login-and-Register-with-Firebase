package com.example.loginandregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;

    String  vEmail, vPassword;
    TextInputLayout email, password;
    Button login, register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btn_submit);
        register = findViewById(R.id.btn_register);
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vEmail = email.getEditText().getText().toString().trim();
                vPassword = password.getEditText().getText().toString().trim();

                if(vEmail.isEmpty()){
                    email.setError("Enter your email");
                    email.requestFocus();
                    return;
                }
                if(vPassword.isEmpty()){
                    password.setError("Enter your password");
                    password.requestFocus();
                    return;
                }
                
                loginUser();
            }
        });
    }
    public void loginUser(){
      mAuth.signInWithEmailAndPassword(vEmail,vPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                  startActivity( new Intent(getApplicationContext(),Data.class));

              }

              else{
                  Toast.makeText(Login.this, "Failed !", Toast.LENGTH_SHORT).show();
              }
          }
      }) ;
    }
}