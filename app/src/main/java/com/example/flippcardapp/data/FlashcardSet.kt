package com.example.flippcardapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_sets")
data class FlashcardSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Int // Color as an Int (e.g., Color.RED)
)
