package com.example.cardapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Friend_Request extends AppCompatActivity {
    Button add;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    TextInputEditText email;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add = findViewById(R.id.btn_add);
        email = findViewById(R.id.friend_name);
        db = FirebaseFirestore.getInstance();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = email.getText().toString();
                DocumentReference docRef = db.collection("users").document(name);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Boolean> map = new HashMap<>();
                                    map.put(name, true);
                                    db.collection(auth.getCurrentUser().getEmail())
                                            .document("friends")
                                            .collection("friends_collection").document(name).set(map);
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                            } else {
                                Log.d(TAG, "Document does not exist!");
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });
    }
}