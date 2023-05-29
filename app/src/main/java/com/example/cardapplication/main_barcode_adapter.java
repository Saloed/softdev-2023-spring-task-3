package com.example.cardapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class main_barcode_adapter extends RecyclerView.Adapter<main_barcode_adapter.MyViewHolder> {

    List<String> movieList;
    List<String> codes;
    private LayoutInflater mInflater;
    Context context;

    public main_barcode_adapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void updateMovieList(List<String> movieList) {
        this.movieList = movieList;
    }

    public void updateCodeList(List<String> codes) {
        this.codes = codes;
    }

    @NonNull
    @Override
    public com.example.cardapplication.main_barcode_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.table_row2, parent, false);
        return new com.example.cardapplication.main_barcode_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.cardapplication.main_barcode_adapter.MyViewHolder holder, int position) {
        String string = movieList.get(position);
        int pos = position;
        holder.textView.setText(string);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                BarcodeFragment barcodeFragment = new BarcodeFragment(codes.get(pos), movieList.get(pos));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, barcodeFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "MyViewHolder";
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.table_name);
        }
    }

}
