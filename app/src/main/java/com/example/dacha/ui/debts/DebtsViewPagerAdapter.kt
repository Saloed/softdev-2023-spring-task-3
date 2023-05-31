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
    val onDebtClicked: ((String) -> Unit)? = null,
    val onDebtLongClicked: ((String, DebtModel) -> Unit)? = null
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
                transactions.forEach { trans ->
                    if (trans.from?.name == item.name && trans.to?.name == it.dPerson && trans.howMuch == it.dAmount.toInt()) {
                        toDelete = true
                        return@forEach
                    }
                }
                if (!toDelete) {
                    youDebt.add("${it.dPerson}    ${it.dAmount.toInt()}P")
                }
            }
            var oldPair = Pair("", 0)
            item.needToGet.forEach {
                var toDelete = false
                transactions.forEach { trans ->
                    if (trans.to?.name == item.name && trans.from?.name == it.first && trans.howMuch == it.second.dAmount.toInt()) {
                        toDelete = true
                        return@forEach
                    }
                }
                if (!toDelete) {
                    oldPair = if (oldPair.first == "") Pair(it.first, it.second.dAmount.toInt())
                    else if (it.first == oldPair.first) Pair(it.first, oldPair.second + it.second.dAmount.toInt())
                    else {
                        whoDebt.add("${oldPair.first}    ${oldPair.second}P")
                        Pair(it.first, it.second.dAmount.toInt())
                    }
                }
            }
            if (oldPair != Pair("", 0)) whoDebt.add("${oldPair.first}    ${oldPair.second}P")
            item.paid.forEach { (s, strings) ->
                paid.addAll(strings)
                events.forEach {
                    if (it.eInfo?.eKey == s) paid.add("${it.eInfo!!.eName.toString()} ${it.eInfo!!.eDate.toString()}:")
                }
            }
            item.bought.forEach { (s, strings) ->
                bought.addAll(strings)
                events.forEach {
                    if (it.eInfo?.eKey == s) bought.add("${it.eInfo!!.eName.toString()} ${it.eInfo!!.eDate.toString()}:")
                }
            }
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
                bought.reversed()
            )
            lvYouPaid.adapter = ArrayAdapter(
                this.binding.root.context,
                android.R.layout.simple_list_item_1,
                paid.reversed()
            )
            lvYouDebt.setOnItemClickListener { _, _, i, _ ->
                onDebtClicked?.invoke(item.needToSend[i].dPerson.toString())
            }
            lvYouDebt.setOnItemLongClickListener { _, _, i, _ ->
                onDebtLongClicked?.invoke(item.name, item.needToSend[i])
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