package com.example.cardapplication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Camera_Fragment extends Fragment{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Bitmap imageBitmap;
    TextView name;
    Button save, back;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_, container, false);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        imageView = view.findViewById(R.id.imageView_fragment);
        name = view.findViewById(R.id.card_name_fragment);
        save = view.findViewById(R.id.saveButton_fragment);
        back = view.findViewById(R.id.dontSave_fragment);


        save.setOnClickListener(this::onSaveButtonClick);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Scanner.class);
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void onSaveButtonClick(View view) {
        if (imageBitmap == null) {
            Toast.makeText(getActivity(), "Сначала нужно сделать фото", Toast.LENGTH_SHORT).show();
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Toast.makeText(getActivity(), "Фото сохранено в base64 формате", Toast.LENGTH_SHORT).show();
        setDataToDataBase(encoded);
    }
    private void setDataToDataBase(String s) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = String.valueOf(auth.getCurrentUser().getEmail());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put(name.getText().toString(), s+"//codeType:Base64");
        db.collection(email).document("card").collection("card_collection")
                .document(name.getText().toString()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().remove(Camera_Fragment.this).commit();
        Intent intent = new Intent(getContext(), Scanner.class);
        startActivity(intent);
    }
}