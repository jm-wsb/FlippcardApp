package com.example.flippcardapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.flippcardapp.R
import com.example.flippcardapp.data.FlashcardSet
import com.example.flippcardapp.databinding.FragmentAddSetBinding

class AddSetFragment : Fragment() {

    private var _binding: FragmentAddSetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveSet.setOnClickListener {
            val name = binding.editTextSetName.text.toString().trim()
            val color = when (binding.radioGroupColors.checkedRadioButtonId) {
                R.id.radioRed -> Color.parseColor("#FFCDD2")
                R.id.radioBlue -> Color.parseColor("#BBDEFB")
                R.id.radioGreen -> Color.parseColor("#C8E6C9")
                R.id.radioYellow -> Color.parseColor("#FFF9C4")
                else -> Color.LTGRAY
            }

            if (name.isNotEmpty()) {
                val set = FlashcardSet(name = name, color = color)
                viewModel.insertSet(set)
                Toast.makeText(context, "Pakiet utworzony!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Podaj nazwę pakietu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
