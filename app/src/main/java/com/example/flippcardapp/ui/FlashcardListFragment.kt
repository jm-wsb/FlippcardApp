package com.example.flippcardapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flippcardapp.R
import com.example.flippcardapp.databinding.FragmentListBinding

class FlashcardListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by viewModels()
    private var setId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setId = arguments?.getInt("setId") ?: -1
        viewModel.setCurrentSet(setId)

        val adapter = FlashcardAdapter { flashcard ->
            AlertDialog.Builder(requireContext())
                .setTitle("Usuwanie fiszki")
                .setMessage("Czy na pewno chcesz usunąć fiszkę \"${flashcard.word}\"?")
                .setPositiveButton("Tak") { _, _ ->
                    viewModel.deleteFlashcard(flashcard)
                }
                .setNegativeButton("Anuluj", null)
                .show()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.currentFlashcards.observe(viewLifecycleOwner) { flashcards ->
            adapter.submitList(flashcards)
        }

        binding.fabAdd.setOnClickListener {
            val bundle = Bundle().apply { putInt("setId", setId) }
            findNavController().navigate(R.id.action_listFragment_to_addFlashcardFragment, bundle)
        }

        binding.fabQuiz.setOnClickListener {
            val bundle = Bundle().apply { putInt("setId", setId) }
            findNavController().navigate(R.id.action_listFragment_to_quizFragment, bundle)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
