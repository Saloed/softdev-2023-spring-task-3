package com.example.dacha.ui.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.PurchaseModel
import com.example.dacha.databinding.PurchaseItemBinding
import com.example.dacha.utils.hide
import com.example.dacha.utils.show


class PurchaseAdapter(val onPurchaseClicked: ((Int, PurchaseModel) -> Unit)? = null) :
    RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {


    private var list: MutableList<PurchaseModel> = arrayListOf()

    val listExp = mutableListOf<Boolean>()


    inner class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PurchaseItemBinding.bind(itemView)
        private val tvProduct = binding.tvPurchaseTop
        private val tvPayer = binding.tvPurchasePayerTop


        private fun openProducts(isExpandable: Boolean) {
            if (isExpandable) {
                binding.expandableLayout.show()
                binding.arrowImageview.setImageResource(R.drawable.arrow_circle_up)
            } else {
                binding.expandableLayout.hide()
                binding.arrowImageview.setImageResource(R.drawable.arrow_circle_down)
            }
        }


        fun bind(item: PurchaseModel, position: Int) {
            tvProduct.text = item.purchaseInfo?.market
            tvPayer.text = "Оплатил: ${item.purchaseInfo?.paid?.name}"

            val childAdapter = ResultProductAdapter()
            item.resultProducts?.values?.let { childAdapter.updateList(it.toMutableList()) }
            binding.childRv.layoutManager = LinearLayoutManager(itemView.context)
            binding.childRv.setHasFixedSize(true)
            binding.childRv.adapter = childAdapter
            binding.purchaseContainer .setOnClickListener {
                onPurchaseClicked?.invoke(bindingAdapterPosition, item)
            }
            binding.arrowImageview.setOnClickListener {
                listExp[bindingAdapterPosition] = !listExp[bindingAdapterPosition]
                openProducts(listExp[bindingAdapterPosition])
                notifyDataSetChanged()
            }
        }
    }

    fun updateList(list: MutableList<PurchaseModel>) {
        this.list = list
        listExp.clear()
        for (i in list.indices) {
            listExp.add(i, false)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.purchase_item, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }
}