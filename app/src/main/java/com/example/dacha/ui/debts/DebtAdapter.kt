package com.example.dacha.ui.debts

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.DebtModel
import com.example.dacha.databinding.SimpleDebtItemBinding
import com.example.dacha.data.model.PersonModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


//class DebtAdapter(private val list: List<DebtModel>, private val name: String, private val listOfProducts: List<ProductModel>) :
//    RecyclerView.Adapter<DebtAdapter.DebtViewHolder>() {
//    class DebtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val binding = SimpleDebtItemBinding.bind(itemView)
//
//        val tvWhom: TextView = binding.tvWhom
//        val tvHowMuch: TextView = binding.tvHowMuch
//        val btnSend: TextView = binding.btnSend
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.simple_debt_item, parent, false)
//        return DebtViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
//        val whom = list[position].dPerson ?: return
//        val howMuch = list[position].dAmount
//
//        holder.tvWhom.text = whom
//
//        holder.tvHowMuch.text = howMuch.toInt().toString() + " Р"
//        val builder = AlertDialog.Builder(holder.binding.root.context)
//
//        val dbPeople = FirebaseDatabase.getInstance().getReference("people")
//        val dbProd = FirebaseDatabase.getInstance().getReference("products").child(name)
//        dbPeople.child(whom).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    var bank: String
//                    var number: String
//                    val pData = snapshot.getValue(PersonModel::class.java)
//                    bank = pData!!.bank ?: ""
//                    number = pData.number ?: ""
//
//                    holder.btnSend.setOnClickListener {
//
//                        builder.setTitle("Скинуть ${howMuch.toInt()} Р")
//                        builder.setMessage("На $bank: $number")
//                        builder.setPositiveButton("СКОПИРОВАТЬ") {_, _ ->
//                            val clipboard = holder.binding.root.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
//                            val clip = ClipData.newPlainText("number", number)
//                            clipboard?.setPrimaryClip(clip)
//                        }
//                        builder.setNeutralButton("ОПЛАТИЛ") { _, _ ->
//                            listOfProducts.forEach {
//                                if (it.pBoughtBy == whom) dbProd.child(it.pKey.toString()).removeValue()
//                            }
//                            Toast.makeText(holder.binding.root.context, "УДАЛЕНЫ ЛИЧНЫЕ ПРОДУКТЫ", Toast.LENGTH_SHORT).show()
//                        }
//                        builder.setNegativeButton("ОТМЕНА") {_,_ ->}
//                        val dialog = builder.create()
//                        dialog.show()
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e(ContentValues.TAG, error.toString())
//            }
//        })
//
//    }
//}