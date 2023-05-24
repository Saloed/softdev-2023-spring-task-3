package com.example.dacha.ui.debts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.*
import com.example.dacha.databinding.DebtViewPagerBinding


class DebtsViewPagerAdapter(
    val onDebtClicked: ((Int, String) -> Unit)? = null,
    val onDebtLongClicked: ((Int, String, DebtModel) -> Unit)? = null
) :
    RecyclerView.Adapter<DebtsViewPagerAdapter.DebtsViewPagerViewHolder>() {

    var debts: MutableList<DebtPresentation> = arrayListOf()
    var events: List<EventModel> = arrayListOf()
    var transactions: List<TransactionModel> = arrayListOf()

    inner class DebtsViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DebtViewPagerBinding.bind(itemView)
        private val tvName = binding.tvDebtName
        private val lvYouDebt = binding.lvYouDebt
        private val lvWhoDebt = binding.lvWhoDebt
        private val lvYouBuy = binding.lvYouBuy
        private val lvYouPaid = binding.lvYouPaid

        fun bind(item: DebtPresentation, position: Int) {
            tvName.text = item.name
            val youDebt = mutableListOf<String>()
            val whoDebt = mutableListOf<String>()
            val paid = mutableListOf<String>()
            val bought = mutableListOf<String>()

            item.needToSend.forEach {
                var toDelete = false
                transactions.forEach {trans ->
                    if (trans.from?.name == item.name && trans.to?.name == it.dPerson && trans.howMuch == it.dAmount.toInt()) {
                        toDelete = true
                        return@forEach
                    }
                }
                if (!toDelete) youDebt.add("${it.dPerson}    ${it.dAmount.toInt()}P")
            }
            item.needToGet.forEach {
                var toDelete = false
                transactions.forEach {trans ->
                    if (trans.to?.name == item.name && trans.from?.name == it.first && trans.howMuch == it.second.dAmount.toInt()) {
                        toDelete = true
                        return@forEach
                    }
                }
                if (!toDelete) whoDebt.add("${it.first}    ${it.second.dAmount.toInt()}P")
            }

            item.paid.forEach { (s, strings) ->
                events.forEach {
                    if (it.eInfo?.eKey == s) paid.add("${it.eInfo!!.eName.toString()} ${it.eInfo!!.eDate.toString()}:")
                }
                paid.addAll(strings)
            }
            item.bought.forEach { (s, strings) ->
                events.forEach {
                    if (it.eInfo?.eKey == s) bought.add("${it.eInfo!!.eName.toString()} ${it.eInfo!!.eDate.toString()}:")
                }
                bought.addAll(strings)
            }
//            val lvYouDebtAdapter = ArrayAdapter(
//                this.binding.root.context,
//                android.R.layout.simple_list_item_1,
//                youDebt
//            )
            lvYouDebt.adapter = ArrayAdapter(
                this.binding.root.context,
                android.R.layout.simple_list_item_1,
                youDebt
            )
            lvWhoDebt.adapter = ArrayAdapter(
                this.binding.root.context,
                android.R.layout.simple_list_item_1,
                whoDebt
            )
            lvYouBuy.adapter = ArrayAdapter(
                this.binding.root.context,
                android.R.layout.simple_list_item_1,
                bought
            )
            lvYouPaid.adapter = ArrayAdapter(
                this.binding.root.context,
                android.R.layout.simple_list_item_1,
                paid
            )
            lvYouDebt.setOnItemClickListener { _, _, i, _ ->
                onDebtClicked?.invoke(position, item.needToSend[i].dPerson.toString())
            }
            lvYouDebt.setOnItemLongClickListener { adapterView, view, i, l ->
                onDebtLongClicked?.invoke(position, item.name, item.needToSend[i])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtsViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.debt_view_pager, parent, false)
        return DebtsViewPagerViewHolder(view)
    }

    fun updateDebts(list: MutableList<DebtPresentation>) {
        debts = list
        notifyDataSetChanged()
    }

    fun updateEvents(list: List<EventModel>) {
        events = list
        notifyDataSetChanged()
    }
    fun updateTransactions(list: List<TransactionModel>) {
        transactions = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return debts.size
    }

    override fun onBindViewHolder(holder: DebtsViewPagerViewHolder, position: Int) {
        val item = debts[position]
        holder.bind(item, position)
    }

}