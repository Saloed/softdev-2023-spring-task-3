package com.example.dacha.ui.products

import android.app.AlertDialog
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.databinding.ItemViewPagerBinding

class ProductsViewPagerAdapter(
) :
    RecyclerView.Adapter<ProductsViewPagerAdapter.ViewPagerViewHolder>() {
    class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemViewPagerBinding.bind(itemView)

        val btnAddPerson: Button = binding.btnAddPerson
        val btnAddProduct: Button = binding.btnAddProduct
        val btnDeletePerson: Button = binding.btnDeletePerson
        val btnDeleteAll: Button = binding.btnDeleteAll
        val btnInfo: Button = binding.btnProductsInfo
        val btnAllPeople: Button = binding.btnAllPeople
        val rCProducts: RecyclerView = binding.rCProducts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
    }
}
