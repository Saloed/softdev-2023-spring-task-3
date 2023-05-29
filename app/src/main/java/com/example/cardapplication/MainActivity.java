package com.example.cardapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardapplication.R.id;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Menu {
    RecyclerView recyclerView;
    private static final long DOUBLE_BACK_PRESS_TIME = 2000;

    private long backPressTime=0;
    MainAdapter adapter;
    List<String> names = new ArrayList<>();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.title_icon);
        recyclerView = findViewById(id.recyclerView);
        db = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MainAdapter(this);
        adapter.setContext(this);
        eventChangeListener();
    }
    @Override
    public void onBackPressed() {
        if (backPressTime + DOUBLE_BACK_PRESS_TIME > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            Intent intent = new Intent(this, Welcome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
            backPressTime = System.currentTimeMillis();
        }
    }

    public void eventChangeListener() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection(auth.getCurrentUser().getEmail()).document("friends").collection("friends_collection").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    names = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        if (!document.getId().endsWith("test")) {
                            names.add(document.getId());
                            System.out.println(document.getString(document.getId()));
                        }
                    }
                    Log.d(TAG, names.toString());
                    if (!names.isEmpty()) {
                        adapter.updateMovieList(names);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void startFavourite(View view) {
        Intent intent = new Intent(this, Favourite_cards.class);
        startActivity(intent);
    }

    public void startScanner(View view) {
        Intent intent = new Intent(this, Scanner.class);
        startActivity(intent);
    }

    public void startMenu(View view) {
        Intent intent = new Intent(this, LogOut.class);
        startActivity(intent);
    }
}