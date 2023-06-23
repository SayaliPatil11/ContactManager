package com.mangoapps.contactmanager.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.common.IListItemClick
import com.mangoapps.contactmanager.databinding.ConatctListItemBinding
import com.mangoapps.contactmanager.model.ContactModel

import java.net.URL


class ContactListAdapter(
    private val itemList: MutableList<ContactModel>,
    context: Context,
    listener: IListItemClick
) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {
    private var mListener: IListItemClick = listener
    private val screenContext = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding =
            ConatctListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

        holder.binding.contactCardView.setOnClickListener {
            mListener.onListItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(var binding: ConatctListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: ContactModel) {
            binding.list = itemView
        }
    }


}