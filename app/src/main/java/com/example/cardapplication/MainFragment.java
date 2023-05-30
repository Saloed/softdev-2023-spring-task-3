package com.example.cardapplication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {
    RecyclerView recyclerView;
    main_barcode_adapter adapter;
    FirebaseFirestore db;
    List<String> names = new ArrayList<>();
    List<String> codes = new ArrayList<>();
    String user_name;
    public MainFragment(String user_name){
        this.user_name=user_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView=view.findViewById(R.id.additional_main_recycler);
        db = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new main_barcode_adapter(view.getContext());
        adapter.setContext(view.getContext());
        initMovies();
        return view;
    }
    private void initMovies() {
        db.collection(user_name)
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
                                }
                            }
                            Log.d(TAG, names.toString());
                            if (!names.isEmpty() && !codes.isEmpty()) {
                                adapter.updateMovieList(names);
                                adapter.updateCodeList(codes);
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}