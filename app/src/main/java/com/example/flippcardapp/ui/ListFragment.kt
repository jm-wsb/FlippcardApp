package com.example.flippcardapp.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flippcardapp.R
import com.example.flippcardapp.databinding.FragmentListBinding
import com.example.flippcardapp.util.adjustForNightMode

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val setId = arguments?.getInt("setId") ?: -1
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

        viewModel.currentSet.observe(viewLifecycleOwner) { set ->
            set?.let {
                val adjustedColor = it.color.adjustForNightMode(requireContext())
                val colorStateList = ColorStateList.valueOf(adjustedColor)
                binding.fabAdd.backgroundTintList = colorStateList
                binding.fabQuiz.backgroundTintList = colorStateList

                val luminance = ColorUtils.calculateLuminance(adjustedColor)
                val iconAndTextColor = if (luminance > 0.5) Color.BLACK else Color.WHITE
                
                binding.fabAdd.imageTintList = ColorStateList.valueOf(iconAndTextColor)
                binding.fabQuiz.iconTint = ColorStateList.valueOf(iconAndTextColor)
                binding.fabQuiz.setTextColor(iconAndTextColor)
            }
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
