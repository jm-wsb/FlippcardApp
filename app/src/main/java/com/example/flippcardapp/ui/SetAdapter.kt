package com.example.flippcardapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flippcardapp.data.FlashcardSet
import com.example.flippcardapp.databinding.ItemSetBinding

class SetAdapter(
    private val onClick: (FlashcardSet) -> Unit,
    private val onLongClick: (FlashcardSet) -> Unit
) : ListAdapter<FlashcardSet, SetAdapter.SetViewHolder>(SetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val binding = ItemSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SetViewHolder(private val binding: ItemSetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(set: FlashcardSet) {
            binding.textViewSetName.text = set.name
            binding.viewColorIndicator.setBackgroundColor(set.color)
            binding.root.setOnClickListener { onClick(set) }
            binding.root.setOnLongClickListener {
                onLongClick(set)
                true
            }
        }
    }

    class SetDiffCallback : DiffUtil.ItemCallback<FlashcardSet>() {
        override fun areItemsTheSame(oldItem: FlashcardSet, newItem: FlashcardSet): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FlashcardSet, newItem: FlashcardSet): Boolean =
            oldItem == newItem
    }
}
