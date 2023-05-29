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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    TextInputEditText editTextemail, editTextPassword;
    TextView textView;
    Button registr;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextemail = findViewById(R.id.email);
        registr = findViewById(R.id.btn_registr);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginView);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        registr.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = editTextemail.getText().toString();
            password = editTextPassword.getText().toString();
            if (email.isEmpty())
                Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
            if (password.isEmpty())
                Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loginCheck", true).commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    addToUsersList(email);
                } else {
                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void addToUsersList(String email) {
        Map<String, Boolean> info = new HashMap<>();
        info.put("test_friend", true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(email).document("friends").collection("friends_collection").document("test").set(info).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getApplicationContext(), "зарегистрирован + создан файл", Toast.LENGTH_SHORT).show();
            }
        });
        Map<String, Integer> card = new HashMap<>();
        card.put("test_friend", 123456);
        db.collection(email).document("card").collection("card_collection").document("test").set(card).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getApplicationContext(), "зарегистрирован + создан файл", Toast.LENGTH_SHORT).show();
            }
        });
        Map<String, Boolean> user = new HashMap<>();
        user.put(email, true);
        db.collection("users").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "зарегистрирован + создан файл", Toast.LENGTH_SHORT).show();
            }
        });
    }
}