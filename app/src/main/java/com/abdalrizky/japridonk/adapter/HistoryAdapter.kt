package com.abdalrizky.japridonk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdalrizky.japridonk.R
import com.abdalrizky.japridonk.database.entity.Recipient
import com.abdalrizky.japridonk.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyData: List<Recipient>): RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onClickCallback
    }

    inner class ItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipient: Recipient) {
            binding.apply {
                tvNumber.text = recipient.number
                tvMessage.text = itemView.context.getString(R.string.message_placeholder, recipient.message ?: "-")
                itemView.setOnClickListener { onItemClickCallback.onItemClicked(recipient) }
                ivClear.setOnClickListener { onItemClickCallback.onClearButtonClicked(recipient) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(historyData[position])
    }

    override fun getItemCount(): Int = historyData.size

    interface OnItemClickCallback {
        fun onItemClicked(recipient: Recipient)
        fun onClearButtonClicked(recipient: Recipient)
    }
}