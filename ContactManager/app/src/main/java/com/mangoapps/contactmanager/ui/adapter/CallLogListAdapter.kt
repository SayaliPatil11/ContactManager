package com.mangoapps.contactmanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.common.IListItemClick
import com.mangoapps.contactmanager.databinding.CallListItemBinding
import com.mangoapps.contactmanager.model.CallLogModel

import java.net.URL


class CallLogListAdapter(
    private val itemList: List<CallLogModel>,
    context: Context,
    listener: IListItemClick
) :
    RecyclerView.Adapter<CallLogListAdapter.ViewHolder>() {
    private var mListener: IListItemClick = listener
    private val screenContext = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding =
            CallListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

        if(itemList[position].name.isNullOrBlank()){
            holder.binding.tvName.text = itemList[position].number
            holder.binding.tvNumber.visibility = View.GONE
        }else{
            holder.binding.tvNumber.visibility = View.VISIBLE
            holder.binding.tvName.text = itemList[position].name
            holder.binding.tvNumber.text = itemList[position].number

        }
        holder.binding.contactCardView.setOnClickListener {
            mListener.onListItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(var binding: CallListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemView: CallLogModel) {
            binding.list = itemView
        }
    }


}