package com.example.loginandregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity  {
String vName,vAge, vEmail, vPassword;
TextInputLayout name, age, email, password;
Button submit;

FirebaseAuth mAuth;
FirebaseDatabase myDb;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        myDb = FirebaseDatabase.getInstance();
        databaseReference = myDb.getReference("Users");

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text from the fields

                vName = name.getEditText().getText().toString().trim();
                vAge = age.getEditText().getText().toString().trim();
                vEmail = email.getEditText().getText().toString().trim();
                vPassword = password.getEditText().getText().toString().trim();

                // Validate

                if(vName.isEmpty()){
                    name.setError("Enter your Full name");
                    name.requestFocus();
                    return;
                }
                if(vAge.isEmpty()){
                    age.setError("Enter your age");
                    age.requestFocus();
                    return;
                }
                if(vEmail.isEmpty()){
                    email.setError("Enter your email");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(vEmail).matches()){
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }
                if(vPassword.isEmpty()){
                    password.setError("Enter a password");
                    password.requestFocus();
                    return;
                }
                if(vPassword.length() < 6){
                    password.setError("Password should be 6 or more characters");
                    password.requestFocus();
                    return;
                }

                // call method for registering user
                register();
            }
        });
    }



    private void register() {


mAuth.createUserWithEmailAndPassword(vEmail,vPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if( task.isSuccessful()){
            UserInfo userInfo = new UserInfo(vName,vAge,vEmail);

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity( new Intent(getApplicationContext(),Login.class));
                    }

                    else{
                        Toast.makeText(MainActivity.this, "Not registered"  , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else{
            Toast.makeText(MainActivity.this, "Failed to register...Try again", Toast.LENGTH_SHORT).show();
        }
    }
});
    }
}