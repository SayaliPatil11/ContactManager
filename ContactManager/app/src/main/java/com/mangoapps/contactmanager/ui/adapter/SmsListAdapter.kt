package com.mangoapps.contactmanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.databinding.ConatctListItemBinding
import com.mangoapps.contactmanager.databinding.SmsListItemBinding
import com.mangoapps.contactmanager.model.ContactModel
import com.mangoapps.contactmanager.model.PermissionModel
import com.mangoapps.contactmanager.model.SMSModel

import java.net.URL


class SmsListAdapter(
    private val itemList: List<SMSModel>,
    context: Context
) :
    RecyclerView.Adapter<SmsListAdapter.ViewHolder>() {
    private val screenContext = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding =
            SmsListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(var binding: SmsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: SMSModel) {
            binding.list = itemView
        }
    }


}