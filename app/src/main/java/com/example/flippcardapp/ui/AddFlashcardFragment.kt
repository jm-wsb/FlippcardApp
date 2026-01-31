package com.example.flippcardapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.flippcardapp.data.Flashcard
import com.example.flippcardapp.databinding.FragmentAddFlashcardBinding

class AddFlashcardFragment : Fragment() {

    private var _binding: FragmentAddFlashcardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by viewModels()
    private var setId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFlashcardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setId = arguments?.getInt("setId") ?: -1

        binding.buttonSave.setOnClickListener {
            val word = binding.editTextWord.text.toString().trim()
            val translation = binding.editTextTranslation.text.toString().trim()

            if (word.isNotEmpty() && translation.isNotEmpty() && setId != -1) {
                val flashcard = Flashcard(setId = setId, word = word, translation = translation)
                viewModel.insertFlashcard(flashcard)
                Toast.makeText(context, "Fiszka zapisana!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
