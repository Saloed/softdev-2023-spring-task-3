package com.example.dacha.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.databinding.SimpleListItemBinding

class PlanProductAdapter(
    val onProductClicked: ((Int, PlanProductModel) -> Unit)? = null
) :
    RecyclerView.Adapter<PlanProductAdapter.PlanProductViewHolder>() {

    private var list: MutableList<PlanProductModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item, parent, false)
        return PlanProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlanProductViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    fun updateList(list: MutableList<PlanProductModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class PlanProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SimpleListItemBinding.bind(itemView)
        private val tvProduct = binding.tvProduct
        private val tvAmount = binding.tvAmount
        private val tvWhose = binding.tvWhose
        fun bind(item: PlanProductModel, position: Int) {
            val name = item.pProduct
            val amount = item.pAmount
            var whose = ""
            if (item.pWhose != null) {
                if (item.pWhose?.size == 1) whose = item.pWhose!![0].name.toString()
                else {
                    for (i in item.pWhose!!) {
                        whose += "${i.name} | "
                    }
                }
            }

            tvProduct.text = name
            tvAmount.text = if (amount.toString().last() == '0') amount?.toInt()
                .toString() else amount.toString()
            tvWhose.text = whose
            binding.planProductItem.setOnClickListener {
                onProductClicked?.invoke(position, item)
            }
        }

    }

}
