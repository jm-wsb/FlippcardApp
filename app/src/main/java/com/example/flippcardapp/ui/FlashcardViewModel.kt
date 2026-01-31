package com.example.flippcardapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.flippcardapp.data.AppDatabase
import com.example.flippcardapp.data.Flashcard
import com.example.flippcardapp.data.FlashcardSet
import kotlinx.coroutines.launch

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {
    private val flashcardDao = AppDatabase.getDatabase(application).flashcardDao()
    
    val allSets: LiveData<List<FlashcardSet>> = flashcardDao.getAllSets()
    val allFlashcards: LiveData<List<Flashcard>> = flashcardDao.getAllFlashcards()

    private val _currentSetId = MutableLiveData<Int>()
    val currentFlashcards: LiveData<List<Flashcard>> = _currentSetId.switchMap { id ->
        flashcardDao.getFlashcardsBySet(id)
    }

    private val _currentQuizCard = MutableLiveData<Flashcard?>()
    val currentQuizCard: LiveData<Flashcard?> = _currentQuizCard

    private val _isShowingTranslation = MutableLiveData(false)
    val isShowingTranslation: LiveData<Boolean> = _isShowingTranslation

    fun setCurrentSet(setId: Int) {
        _currentSetId.value = setId
    }

    fun insertSet(set: FlashcardSet) = viewModelScope.launch {
        flashcardDao.insertSet(set)
    }

    fun deleteSet(set: FlashcardSet) = viewModelScope.launch {
        flashcardDao.deleteSet(set)
    }

    fun insertFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        flashcardDao.insertFlashcard(flashcard)
    }

    fun deleteFlashcard(flashcard: Flashcard) = viewModelScope.launch {
        flashcardDao.deleteFlashcard(flashcard)
    }

    fun delete(flashcard: Flashcard) = deleteFlashcard(flashcard)

    fun loadNextQuizCard(setId: Int, shuffle: Boolean = true) = viewModelScope.launch {
        if (shuffle) {
            _currentQuizCard.value = flashcardDao.getRandomFlashcardFromSet(setId)
        } else {
            _currentQuizCard.value = flashcardDao.getRandomFlashcardFromSet(setId)
        }
        _isShowingTranslation.value = false
    }

    fun toggleTranslation() {
        _isShowingTranslation.value = !(_isShowingTranslation.value ?: false)
    }
}
