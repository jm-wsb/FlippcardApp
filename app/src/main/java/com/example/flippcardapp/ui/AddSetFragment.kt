package com.example.flippcardapp.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import com.example.flippcardapp.data.FlashcardSet
import com.example.flippcardapp.databinding.FragmentAddSetBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddSetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddSetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by viewModels()
    private var selectedColor: Int = Color.parseColor("#BBDEFB")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSelectedColor(selectedColor)

        binding.buttonPickColor.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())
                .setTitle("Wybierz kolor")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(selectedColor)
                .setColorListener { color, _ ->
                    updateSelectedColor(color)
                }
                .show()
        }

        binding.buttonSaveSet.setOnClickListener {
            val name = binding.editTextSetName.text.toString().trim()

            if (name.isNotEmpty()) {
                val set = FlashcardSet(name = name, color = selectedColor)
                viewModel.insertSet(set)
                Toast.makeText(context, "Pakiet utworzony!", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "Podaj nazwę pakietu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSelectedColor(color: Int) {
        selectedColor = color
        binding.viewSelectedColor.setBackgroundColor(color)
        
        // Dynamiczna zmiana koloru tekstu na przycisku zapisu
        binding.buttonSaveSet.backgroundTintList = ColorStateList.valueOf(color)
        val luminance = ColorUtils.calculateLuminance(color)
        val textColor = if (luminance > 0.5) Color.BLACK else Color.WHITE
        binding.buttonSaveSet.setTextColor(textColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
