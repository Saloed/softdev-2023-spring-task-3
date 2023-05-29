package com.example.cardapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friend_request:
                Log.d(TAG, "onMenuItemClick: friend request " + item.getItemId());
                Intent intent =new Intent(getApplicationContext(), Friend_Request.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
