package com.example.flippcardapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flippcardapp.R
import com.example.flippcardapp.databinding.FragmentSetListBinding

class SetListFragment : Fragment() {

    private var _binding: FragmentSetListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashcardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SetAdapter(
            onClick = { set ->
                val bundle = Bundle().apply { putInt("setId", set.id) }
                findNavController().navigate(R.id.action_setListFragment_to_listFragment, bundle)
            },
            onLongClick = { set ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Usuwanie pakietu")
                    .setMessage("Czy na pewno chcesz usunąć pakiet \"${set.name}\"?")
                    .setPositiveButton("Tak") { _, _ ->
                        viewModel.deleteSet(set)
                    }
                    .setNegativeButton("Anuluj", null)
                    .show()
            }
        )

        binding.recyclerViewSets.adapter = adapter
        binding.recyclerViewSets.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allSets.observe(viewLifecycleOwner) { sets ->
            adapter.submitList(sets)
        }

        binding.fabAddSet.setOnClickListener {
            findNavController().navigate(R.id.action_setListFragment_to_addSetFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
