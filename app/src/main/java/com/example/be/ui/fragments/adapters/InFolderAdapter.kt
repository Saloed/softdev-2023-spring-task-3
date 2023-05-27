package com.example.be.ui.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.be.R
import com.example.be.databinding.NewFolderBinding
import com.example.be.databinding.NewMessageBinding
import com.example.be.models.Folder
import com.example.be.models.Message
import com.example.be.utilits.FOLDER
import com.example.be.utilits.MESSAGE

class InFolderAdapter(var listener: OnItemClickListener?): RecyclerView.Adapter<InFolderAdapter.InFolderHolder>() {

    private var listMessages = ArrayList<Message>()

    interface OnItemClickListener {
        fun onFolderClick(message: Message)
        fun onDeleteClick(message: Message)
        fun onEditClick(message: Message)
    }


    class InFolderHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = NewMessageBinding.bind(item)

        fun bind(message: Message, listener: OnItemClickListener?) = with(binding) {
            titleMessage.text = message.title

            itemView.setOnClickListener {
                MESSAGE = Message(message.text, message.type, message.id, message.title)
                listener?.onFolderClick(message)
            }
            btnDeleteMessage.setOnClickListener {
                Log.d("MyLog", "onDeleteClick")
                listener?.onDeleteClick(message)
            }
            btnEditTitleMessage.setOnClickListener {
                MESSAGE = Message(message.text, message.type, message.id, message.title)
                listener?.onEditClick(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InFolderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_message, parent, false)
        return InFolderHolder(view)
    }

    override fun getItemCount(): Int = listMessages.size

    override fun onBindViewHolder(holder: InFolderHolder, position: Int) {
        holder.bind(listMessages[position], listener)
    }

    fun setList(list: ArrayList<Message>) {
        listMessages = list
        /*notifyDataSetChanged()*//*данныйе изменены и их надо обработать*/
    }

}