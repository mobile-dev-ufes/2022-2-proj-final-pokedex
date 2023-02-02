package com.example.pokedex.ui.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.databinding.FragmentTypesBinding
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.ui.recycleView.typeRelations.ListTypeRelationAdapter
import com.example.pokedex.ui.recycleView.types.ListTypesAdapter
import com.example.pokedex.ui.recycleView.types.OnTypeListener

class TypeFragment : Fragment() {

    private var _binding: FragmentTypesBinding? = null
    private lateinit var typeViewModel: TypeViewModel
    private val typeAdapter = ListTypesAdapter()
    private val typeDefenseAdapter = ListTypeRelationAdapter()
    private val typeAttackAdapter = ListTypeRelationAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        typeViewModel = ViewModelProvider(this)[TypeViewModel::class.java]

        _binding = FragmentTypesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListTypes.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListTypes.adapter = typeAdapter

        binding.recyclerListDefense.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListDefense.adapter = typeDefenseAdapter

        binding.recyclerListAttack.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListAttack.adapter = typeAttackAdapter

        val listener = object : OnTypeListener {
            override fun onClick(type: TypeEntity) {
                typeViewModel.userSelectType(type)
            }
        }
        typeAdapter.setListener(listener)

        typeViewModel.getAllTypes()

        setObserver()

        return root
    }

    private fun setObserver() {
        typeViewModel.getTypeList().observe(viewLifecycleOwner) {
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {
                typeColors.add(
                    resources.getColor(
                        com.example.pokedex.utils.Resources.getColorByName(it.name), null
                    )
                )
                typeStrings.add(
                    resources.getString(
                        com.example.pokedex.utils.Resources.getStringByName(it.name)
                    )
                )
            }
            typeAdapter.updateColors(typeColors.toList())
            typeAdapter.updateNames(typeStrings.toList())
            typeAdapter.updateTypeList(it)

        }

        typeViewModel.getTypeEffectiveness().observe(viewLifecycleOwner) {
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {
                typeColors.add(
                    resources.getColor(
                        com.example.pokedex.utils.Resources.getColorByName(it.name), null
                    )
                )
                typeStrings.add(
                    resources.getString(
                        com.example.pokedex.utils.Resources.getStringByName(it.name)
                    )
                )
            }
            typeAttackAdapter.updateColorsList(typeColors.toList())
            typeAttackAdapter.updateNamesList(typeStrings.toList())
            typeAttackAdapter.updateTypeList(it)
        }

        typeViewModel.getTypeWeakness().observe(viewLifecycleOwner) {
            val typeColors: MutableList<Int> = mutableListOf()
            val typeStrings: MutableList<String> = mutableListOf()
            it.forEach {
                typeColors.add(
                    resources.getColor(
                        com.example.pokedex.utils.Resources.getColorByName(it.name), null
                    )
                )
                typeStrings.add(
                    resources.getString(
                        com.example.pokedex.utils.Resources.getStringByName(it.name)
                    )
                )
            }
            typeDefenseAdapter.updateColorsList(typeColors.toList())
            typeDefenseAdapter.updateNamesList(typeStrings.toList())
            typeDefenseAdapter.updateTypeList(it)
        }

        typeViewModel.getSelectedTypeList().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.recyclerListDefense.visibility = View.GONE
                binding.textDefense.visibility = View.GONE
                binding.recyclerListAttack.visibility = View.GONE
                binding.textAttack.visibility = View.GONE
            } else if (it.size == 1) {
                binding.recyclerListDefense.visibility = View.VISIBLE
                binding.textDefense.visibility = View.VISIBLE
                binding.recyclerListAttack.visibility = View.VISIBLE
                binding.textAttack.visibility = View.VISIBLE
            } else {
                binding.recyclerListDefense.visibility = View.VISIBLE
                binding.textDefense.visibility = View.VISIBLE
                binding.recyclerListAttack.visibility = View.GONE
                binding.textAttack.visibility = View.GONE
            }
            typeAdapter.select(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
