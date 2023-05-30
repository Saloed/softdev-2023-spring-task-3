package com.example.cardapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    List<String> movieList;
    List<String> codes;
    private LayoutInflater mInflater;
    Context context;

    public RecyclerAdapter(Context context) {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.table_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String string = movieList.get(position);
        int pos = position;
        holder.textView.setText(string);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                BarcodeFragment barcodeFragment = new BarcodeFragment(codes.get(pos), movieList.get(pos));
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.barcode_frame, barcodeFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView textView;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.table_name);
            imageButton = itemView.findViewById(R.id.table_menu);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.side_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String documentId = movieList.get(getAdapterPosition());

                    db.collection(auth.getCurrentUser().getEmail())
                            .document("card").collection("card_collection").document(documentId)
                            .delete();
                    return true;
                default:
                    return false;
            }
        }
    }

}
