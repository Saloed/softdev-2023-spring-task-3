package com.example.dacha.ui.people


import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.PersonItemBinding
import com.google.firebase.database.FirebaseDatabase

class PeopleAdapter(
    val onDeleteClicked: ((Int, PersonModel) -> Unit)? = null,
    val onPersonClicked: ((Int, PersonModel) -> Unit)? = null
) :
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    private var list: MutableList<PersonModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return PeopleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    fun updateList(list: MutableList<PersonModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyDataSetChanged()
        //notifyItemRemoved(position)
    }

    inner class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PersonItemBinding.bind(itemView)
        private val tvName = binding.tVName
        private val tvBank = binding.tVBank
        private val tvNumber = binding.tVNumber
        private val btnDelete = binding.btnDeletePerson
        fun bind(item: PersonModel, position: Int) {
            val name = item.name
            val bank = item.bank
            val number = item.number
            tvName.text = name
            if (bank == null) tvBank.visibility = View.GONE
            else {
                tvBank.text = bank
                tvBank.visibility = View.VISIBLE
            }
            if (number == null) tvNumber.visibility = View.GONE
            else {
                tvNumber.text = number
                tvNumber.visibility = View.VISIBLE
            }
            btnDelete.setOnClickListener {
                onDeleteClicked?.invoke(position, item)
            }
            binding.personItem.setOnClickListener {
                onPersonClicked?.invoke(position, item)
            }
        }

    }
}











//val dbRef = FirebaseDatabase.getInstance().getReference("people").child(name.toString())
//holder.binding.personItem.setOnLongClickListener {
//    val builder = AlertDialog.Builder(holder.binding.root.context)
//    builder.setTitle(name)
//    builder.setMessage("Хотите изменить данные?")
//    val inputBank = EditText(holder.binding.root.context)
//    inputBank.hint = "Введите, куда Вам скинуть"
//    val inputNumber = EditText(holder.binding.root.context)
//    inputNumber.hint = "Введите номер"
//
//    val ll = LinearLayout(holder.binding.root.context)
//
//    ll.orientation = LinearLayout.VERTICAL
//    ll.addView(inputBank)
//    ll.addView(inputNumber)
//    builder.setView(ll)
//
//    builder.setPositiveButton("ИЗМЕНИТЬ") { _, _ ->
//        val newBank = inputBank.text.toString()
//        val newNumber = inputNumber.text.toString()
//        if (newBank != "") dbRef.child("bank").setValue(newBank)
//        if (newNumber != "") dbRef.child("number").setValue(newNumber)
//    }
//    builder.setNeutralButton("ОТМЕНА") { _, _ -> }
//    builder.setNegativeButton("УДАЛИТЬ ЧЕЛОВЕКА") {_, _ ->
//        dbRef.removeValue().addOnCompleteListener {
//            Toast.makeText(holder.binding.root.context, "$name удален", Toast.LENGTH_LONG)
//                .show()
//        }.addOnCanceledListener {
//            Toast.makeText(
//                holder.binding.root.context,
//                "$name не удален",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//    builder.show()
//    true
//}