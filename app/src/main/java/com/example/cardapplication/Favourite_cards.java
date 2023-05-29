package com.example.cardapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Favourite_cards extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    FrameLayout frameLayout;
    List<String> names = new ArrayList<>();
    List<String> codes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_cards);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.cards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        frameLayout = findViewById(R.id.barcode_frame);
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerAdapter.setContext(this);
        initMovies();
    }

    private void initMovies() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection(auth.getCurrentUser().getEmail())
                .document("card").collection("card_collection")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            names = new ArrayList<>();
                            codes = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                if (!document.getId().endsWith("test")) {
                                    names.add(document.getId());
                                    codes.add(document.getString(document.getId()));
                                    System.out.println(document.getString(document.getId()));
                                }
                            }
                            Log.d(TAG, names.toString());
                            if (!names.isEmpty() && !codes.isEmpty()) {
                                recyclerAdapter.updateMovieList(names);
                                recyclerAdapter.updateCodeList(codes);
                                recyclerView.setAdapter(recyclerAdapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    }