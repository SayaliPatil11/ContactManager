package com.mangoapps.contactmanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.databinding.PermissionViewBinding
import com.mangoapps.contactmanager.model.PermissionModel


class PermissionListAdapter(
    private val itemList: List<PermissionModel>
) :
    RecyclerView.Adapter<PermissionListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding =
            PermissionViewBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(var binding: PermissionViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: PermissionModel) {
            binding.list = itemView
        }
    }


}