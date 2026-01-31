package com.example.flippcardapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = FlashcardSet::class,
            parentColumns = ["id"],
            childColumns = ["setId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["setId"])]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val setId: Int,
    val word: String,
    val translation: String,
    val description: String = "",
    val difficulty: Int = 0
)
