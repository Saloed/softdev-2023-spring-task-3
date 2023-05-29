package com.example.cardapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Barcode_Image extends AppCompatActivity {
    String code;
    Barcode_Image(String code){
        this.code=code;
    }
    Barcode_Image(){}
    MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_image);
        imageView=findViewById(R.id.barcode_image);
        try {
            barcode();}
        catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
    private  void barcode() throws WriterException {
        BitMatrix bitMatrix=multiFormatWriter.encode(code, BarcodeFormat.CODE_128,400,300);
        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
        imageView.setImageBitmap(bitmap);

    }
}