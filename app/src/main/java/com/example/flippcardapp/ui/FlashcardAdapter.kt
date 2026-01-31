package com.example.flippcardapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flippcardapp.data.Flashcard
import com.example.flippcardapp.databinding.ItemFlashcardBinding

class FlashcardAdapter(private val onDelete: (Flashcard) -> Unit) :
    ListAdapter<Flashcard, FlashcardAdapter.FlashcardViewHolder>(FlashcardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val binding = ItemFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashcardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FlashcardViewHolder(private val binding: ItemFlashcardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flashcard: Flashcard) {
            binding.textViewWord.text = flashcard.word
            binding.textViewTranslation.text = flashcard.translation
            binding.root.setOnLongClickListener {
                onDelete(flashcard)
                true
            }
        }
    }

    class FlashcardDiffCallback : DiffUtil.ItemCallback<Flashcard>() {
        override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean =
            oldItem == newItem
    }
}
