package com.example.dacha.ui.products

import android.app.AlertDialog
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.databinding.SimpleListItemBinding

//class ProductAdapter(
//) :
//    RecyclerView.Adapter<ProductAdapter.SimpleViewHolder>() {
//    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val binding = SimpleListItemBinding.bind(itemView)
//        val tvProduct: TextView = binding.tvProduct
//        val tvAmount: TextView = binding.tvAmount
//        val tvPrice: TextView = binding.tvPrice
//        val tvBoughtBy: TextView = binding.tvBoughtBy
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item, parent, false)
//        return SimpleViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return 0
//    }
//
//    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
//
//    }
//}