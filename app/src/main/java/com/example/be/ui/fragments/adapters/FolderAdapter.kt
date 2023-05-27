package com.example.be.ui.fragments.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.be.R
import com.example.be.databinding.NewFolderBinding
import com.example.be.models.Folder
import com.example.be.utilits.FOLDER


class FolderAdapter(private val mList: ArrayList<Folder>, var listener: OnItemClickListener?): RecyclerView.Adapter<FolderAdapter.FolderHolder>() {

    interface OnItemClickListener {
        fun onFolderClick(folder: Folder)
        fun onDeleteClick(folder: Folder)
        fun onEditClick(folder: Folder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        /*создает FolderHoder и туда передает разметку, котрую можно заполнять уже*/
        /*берет разметку и превращает ее в реальный view*/
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.new_folder, parent, false)
        return FolderHolder(view)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {/*начинаем заполнять view который уже в памяти*/
        holder.bind(mList[position], listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class FolderHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = NewFolderBinding.bind(item)

        fun bind(folder: Folder, listener: OnItemClickListener?) = with(binding) {
            imageView.setImageResource(R.drawable.baseline_folder_24)
            nameFolder.text = folder.name

            itemView.setOnClickListener {
                FOLDER = Folder(folder.name, folder.id)
                listener?.onFolderClick(folder)
            }
            btnDelete.setOnClickListener {
                Log.d("MyLog", "onDeleteClick")
                listener?.onDeleteClick(folder)
            }
            btnEdit.setOnClickListener {
                FOLDER = Folder(folder.name, folder.id)
                listener?.onEditClick(folder)
            }
        }
    }

}