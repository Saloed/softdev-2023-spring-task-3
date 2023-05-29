package com.example.cardapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CardSave extends Fragment {
    String string;
    CardSave(String string){
        this.string=string;
    }
    Button save,back;
    EditText cardCode,cardName;
    String nameString,codeString;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_save, container, false);
        back=v.findViewById(R.id.dontSave_fragment);
        cardCode = v.findViewById(R.id.card_numb_fragment);
        cardName = v.findViewById(R.id.card_name_fragment);
        save = v.findViewById(R.id.saveButton_fragment);
        cardCode.setText(string);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString=cardName.getText().toString();
                codeString=cardCode.getText().toString();
                addCard();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CardSave.this).commit();
                Intent intent =new Intent(getContext(),Scanner.class);
                startActivity(intent);
            }
        });
        return v;
    }
    private void addCard(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String email= String.valueOf(auth.getCurrentUser().getEmail());
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String,String> map=new HashMap<>();
        map.put(nameString,codeString);
        db.collection(email).document("card").collection("card_collection")
                .document(nameString).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "ура победа должны появиться карты", Toast.LENGTH_LONG).show();
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().remove(CardSave.this).commit();
        Intent intent =new Intent(getContext(),Scanner.class);
        startActivity(intent);
    }
}