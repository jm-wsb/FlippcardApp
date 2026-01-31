package com.example.flippcardapp.network

import com.example.flippcardapp.data.Flashcard
import retrofit2.http.GET

interface ApiService {
    // A mock endpoint or a sample gist containing flashcards
    @GET("flashcards.json")
    suspend fun getFlashcards(): List<Flashcard>
}
