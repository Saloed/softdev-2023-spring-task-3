package com.example.cardapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends AppCompatActivity {
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
        firebaseAuth=FirebaseAuth.getInstance();
        Handler handler=new Handler();
        handler.postDelayed(this::checkAuth,500);
    }
    void checkAuth(){
        Intent intent;
        System.out.println("идет проверка");
        SharedPreferences sharedPreferences =  getSharedPreferences("Login", Context.MODE_PRIVATE);
        boolean check=false;
        try {
            check=sharedPreferences.getAll().containsKey("loginCheck");
        }
        catch (Exception e){
        }

        if (check==false){

            intent= new Intent(this, Login.class);
        }
        else {
            System.out.println(firebaseAuth.getCurrentUser().getEmail());
            intent= new Intent(this, MainActivity.class);
        }
        startActivity(intent);
    }
}