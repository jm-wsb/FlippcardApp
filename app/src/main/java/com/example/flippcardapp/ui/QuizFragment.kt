package com.example.flippcardapp.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.flippcardapp.R
import com.example.flippcardapp.databinding.FragmentQuizBinding
import com.example.flippcardapp.util.adjustForNightMode

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by activityViewModels()
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
        viewModel.setCurrentSet(setId)

        // Ustawienie dystansu kamery, aby uniknąć wychodzenia karty przed przycisk
        val scale = resources.displayMetrics.density
        binding.cardView.cameraDistance = 8000 * scale

        if (viewModel.currentQuizCard.value == null) {
            viewModel.loadNextQuizCard(setId)
        }

        viewModel.currentSet.observe(viewLifecycleOwner) { set ->
            set?.let {
                val adjustedColor = it.color.adjustForNightMode(requireContext())
                binding.buttonNext.backgroundTintList = ColorStateList.valueOf(adjustedColor)

                val luminance = ColorUtils.calculateLuminance(adjustedColor)
                val textColor = if (luminance > 0.5) Color.BLACK else Color.WHITE
                binding.buttonNext.setTextColor(textColor)
            }
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
