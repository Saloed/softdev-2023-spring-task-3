package com.example.cardapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

public class Scanner extends AppCompatActivity {
    private static final String TAG = Scanner.class.getSimpleName();
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private CodeScanner mCodeScanner;
    private CodeScannerView mScannerView;
    ImageButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mScannerView = findViewById(R.id.scanner);
        mCodeScanner = new CodeScanner(this, mScannerView);
        button=findViewById(R.id.open_camera);
        checkCameraPermission(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera_Fragment fragment=new Camera_Fragment();
                setFragment(fragment);
            }
        });
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                        CardSave cardSave=new CardSave(result+"//codeType:"+result.getBarcodeFormat());
                        setFragment(cardSave);
            }
        });
        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Throwable thrown) {
                Log.e(TAG, "Error: " + thrown.getMessage());
            }
        });


    }
        public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void checkCameraPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        } else {
            requestCameraPermission(activity);
        }
    }

    private void requestCameraPermission(final Activity activity) { // принимаем активити вместо контекста
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) { // передаем активити вместо контекста
            Snackbar.make(mScannerView, "Нужно ваше разрешение на использование камеры", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(activity, // передаем активити вместо контекста
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(activity, // передаем активити вместо контекста
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            }
        }
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startScanning() {
        mCodeScanner.startPreview();
    }
}