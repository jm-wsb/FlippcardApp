package com.example.flippcardapp.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.flippcardapp.R
import com.example.flippcardapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by viewModels()
    private var setId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setId = arguments?.getInt("setId") ?: -1

        if (viewModel.currentQuizCard.value == null) {
            viewModel.loadNextQuizCard(setId)
        }

        viewModel.currentQuizCard.observe(viewLifecycleOwner) { flashcard ->
            if (flashcard != null) {
                updateCardText()
            } else {
                binding.textViewContent.text = "Brak fiszek w tym pakiecie."
            }
        }

        viewModel.isShowingTranslation.observe(viewLifecycleOwner) {
            updateCardText()
        }

        binding.cardView.setOnClickListener {
            if (viewModel.currentQuizCard.value != null) {
                animateFlip()
            }
        }

        binding.buttonNext.setOnClickListener {
            viewModel.loadNextQuizCard(setId)
        }
    }

    private fun updateCardText() {
        val flashcard = viewModel.currentQuizCard.value ?: return
        val isTranslation = viewModel.isShowingTranslation.value ?: false
        binding.textViewContent.text = if (isTranslation) flashcard.translation else flashcard.word
    }

    private fun animateFlip() {
        val outAnim = AnimatorInflater.loadAnimator(context, R.animator.card_flip_out) as AnimatorSet
        val inAnim = AnimatorInflater.loadAnimator(context, R.animator.card_flip_in) as AnimatorSet
        
        outAnim.setTarget(binding.cardView)
        inAnim.setTarget(binding.cardView)

        outAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                viewModel.toggleTranslation()
                inAnim.start()
            }
        })
        outAnim.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
