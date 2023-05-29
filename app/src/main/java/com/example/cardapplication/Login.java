package com.example.cardapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputEditText editTextemail,editTextPassword;
    TextView textView;
    Button login;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        mAuth= FirebaseAuth.getInstance();
        editTextemail=findViewById(R.id.email);
        login=findViewById(R.id.btn_login);
        editTextPassword=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.registerView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email=editTextemail.getText().toString();
                password=editTextPassword.getText().toString();
                if (email.isEmpty()) Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                if (password.isEmpty()) Toast.makeText(Login.this, "Enter passwod", Toast.LENGTH_SHORT).show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences =  getSharedPreferences("Login", Context.MODE_PRIVATE);;
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putBoolean("loginCheck",true).commit();
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
}
}