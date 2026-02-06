package com.example.flippcardapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FlashcardDao {
    // Sets
    @Query("SELECT * FROM flashcard_sets")
    fun getAllSets(): LiveData<List<FlashcardSet>>

    @Query("SELECT * FROM flashcard_sets WHERE id = :id")
    fun getSetById(id: Int): LiveData<FlashcardSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: FlashcardSet): Long

    @Delete
    suspend fun deleteSet(set: FlashcardSet)

    // Flashcards
    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): LiveData<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE setId = :setId")
    fun getFlashcardsBySet(setId: Int): LiveData<List<Flashcard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM flashcards WHERE setId = :setId ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomFlashcardFromSet(setId: Int): Flashcard?

    @Query("SELECT * FROM flashcards ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomFlashcard(): Flashcard?
    
    @Query("SELECT * FROM flashcards WHERE setId = :setId")
    suspend fun getFlashcardsBySetSync(setId: Int): List<Flashcard>
}
