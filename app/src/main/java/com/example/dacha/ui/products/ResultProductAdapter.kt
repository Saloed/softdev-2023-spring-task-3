package com.example.dacha.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.data.model.ResultProductModel
import com.example.dacha.databinding.ResultProductItemBinding

class ResultProductAdapter() :
    RecyclerView.Adapter<ResultProductAdapter.ResultProductViewHolder>() {

    private var list: MutableList<ResultProductModel> = arrayListOf()

    inner class ResultProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ResultProductItemBinding.bind(itemView)
        private val tvProduct = binding.tvProduct
        private val tvAmount = binding.tvAmount
        private val tvPrice = binding.tvPrice
        private val tvWhose = binding.tvWhose
        fun bind(item: ResultProductModel, position: Int) {
            val name = item.rProduct
            val amount = item.rAmount
            val price = item.pPrice?.toInt()
            var whose = ""
            if (item.rWhose != null){
                if (item.rWhose?.size == 1) whose = item.rWhose!![0].name.toString()
                else {
                    for (i in item.rWhose!!) {
                        whose += "${i.name} | "
                    }
                }
            }
            tvPrice.text = "${price.toString()} Р"
            tvProduct.text = name
            tvAmount.text = amount.toString()
            tvWhose.text = whose
        }
    }

    fun updateList(list: MutableList<ResultProductModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.result_product_item, parent, false)
        return ResultProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResultProductViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }
}