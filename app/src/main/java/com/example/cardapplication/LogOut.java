package com.example.cardapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends AppCompatActivity {
FirebaseAuth auth;
Button button;
TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth=FirebaseAuth.getInstance();
        button=findViewById(R.id.logout);
        textView=findViewById(R.id.users_details);
        if (auth.getCurrentUser()==null){
            Intent intent=new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(auth.getCurrentUser().getEmail());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences sharedPreferences =  getSharedPreferences("Login", Context.MODE_PRIVATE);;
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear().commit();
                    Intent intent=new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}