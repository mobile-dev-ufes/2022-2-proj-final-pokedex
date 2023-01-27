package com.example.pokedex.ui.dashboard

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.databinding.FragmentDashboardBinding
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.ui.recycleView.typeRelations.ListTypeRelationAdapter
import com.example.pokedex.ui.recycleView.types.ListTypesAdapter
import com.example.pokedex.ui.recycleView.types.OnTypeListener

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var dashboardViewModel: DashboardViewModel
    private val typeAdapter = ListTypesAdapter()
    private val typeDefenseAdapter = ListTypeRelationAdapter()
    private val typeAttackAdapter = ListTypeRelationAdapter()
    private var selectedType: TypeEntity? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListTypes.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListTypes.adapter = typeAdapter

        binding.recyclerListDefense.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListDefense.adapter = typeDefenseAdapter

        binding.recyclerListAttack.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerListAttack.adapter = typeAttackAdapter

        val listener = object : OnTypeListener {
            override fun onClick(type: TypeEntity) {
                dashboardViewModel.userSelectType(type)
            }
        }
        typeAdapter.setListener(listener)

        dashboardViewModel.getAllTypes()

        setObserver()

        return root
    }

    private fun setObserver() {
        dashboardViewModel.getTypeList().observe(viewLifecycleOwner, Observer {
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

        })

        dashboardViewModel.getTypeEffectiveness().observe(viewLifecycleOwner, Observer {
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
        })

        dashboardViewModel.getTypeWeakness().observe(viewLifecycleOwner, Observer {
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
        })

        dashboardViewModel.getSelectedTypeList().observe(viewLifecycleOwner, Observer {
            if(it.size != 1) {
                binding.recyclerListAttack.visibility = View.GONE
                binding.textAttack.visibility = View.GONE
            } else {
                binding.recyclerListAttack.visibility = View.VISIBLE
                binding.textAttack.visibility = View.VISIBLE
            }
            typeAdapter.select(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
