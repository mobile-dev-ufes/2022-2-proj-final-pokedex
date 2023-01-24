package com.example.pokedex.ui.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonBinding
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.repository.database.model.PokemonEntity
import com.example.pokedex.ui.recycleView.evolution.ListEvolutionAdapter
import com.example.pokedex.ui.recycleView.evolution.OnEvolutionListener
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Resources


class PokemonFragment : Fragment() {

    private var _binding: FragmentPokemonBinding? = null
    private lateinit var pokemonViewModel: PokemonViewModel
    private val args: PokemonFragmentArgs by navArgs()
    private val evolutionAdapter = ListEvolutionAdapter()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pokemonViewModel = ViewModelProvider(this)[PokemonViewModel::class.java]

        _binding = FragmentPokemonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerListEvolutions()

        configureListeners()

        setObserver()

        pokemonViewModel.loadPokemon(args.pokemonId)

        return root
    }

    private fun initRecyclerListEvolutions() {
        binding.recyclerListEvolutions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerListEvolutions.adapter = evolutionAdapter

        val listener = object : OnEvolutionListener {
            override fun onClick(evolution: EvolutionEntity) {
                if (evolution.id == args.pokemonId)
                    return

                val navBuilder = NavOptions.Builder()
                if (evolution.id > args.pokemonId)
                    navBuilder.setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_right).setPopExitAnim(R.anim.slide_out_right)
                else
                    navBuilder.setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_right)
                        .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_left)

                val action = PokemonFragmentDirections.actionPokemonFragmentSelf(evolution.id)
                findNavController().navigate(action, navBuilder.build())
            }
        }
        evolutionAdapter.setListener(listener)
    }

    private fun configureListeners() {
        binding.shinyButton.setOnClickListener {
            pokemonViewModel.setShinyButtonToggle()
        }
        binding.maleButton.setOnClickListener{
            pokemonViewModel.setGenderButtons(Constants.GENDERS.MALE)
        }
        binding.femaleButton.setOnClickListener{
            pokemonViewModel.setGenderButtons(Constants.GENDERS.FEMALE)
        }
        binding.backImage.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun setObserver() {
        pokemonViewModel.getPokemon.observe(viewLifecycleOwner) {
            configurePokemonTypes(it)
            configurePokemonStats(it)
        }
        pokemonViewModel.getImgDefault.observe(viewLifecycleOwner) {
            binding.pokemonImage.setImageBitmap(it)
        }
        pokemonViewModel.getShinyButton.observe(viewLifecycleOwner) {
            configurePokemonImage(it, pokemonViewModel.getGenderButtons.value!!)
            configureShinyImage(it)
        }
        pokemonViewModel.getGenderButtons.observe(viewLifecycleOwner) {
            configurePokemonImage(pokemonViewModel.getShinyButton.value!!, it)
            configureGenderButtons(it)
        }
        pokemonViewModel.getCustomEvolutionList.observe(viewLifecycleOwner) {
            evolutionAdapter.updateEvolutionList(it)
        }
    }

    private fun configurePokemonTypes(pokemonEntity: PokemonEntity){
        binding.pokemonName.text = pokemonEntity.name.replaceFirstChar { it.uppercase() }
        binding.pokemonType1.text = resources.getString(Resources.getStringByName(pokemonEntity.typeOne))
        binding.pokemonType1.setBackgroundColor(resources.getColor(Resources.getColorByName(pokemonEntity.typeOne), null))
        if (pokemonEntity.typeTwo != null) {
            binding.pokemonType2.text = resources.getString(Resources.getStringByName(pokemonEntity.typeTwo!!))
            binding.pokemonType2.setBackgroundColor(resources.getColor(Resources.getColorByName(pokemonEntity.typeTwo!!), null))
        }else{
            binding.pokemonType1.layoutParams =
                LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    marginEnd = 0
                }
            binding.pokemonType2.text = ""
            binding.pokemonType2.width = 0
            binding.pokemonType2.height = 0
        }
    }

    private fun configurePokemonStats(pokemonEntity: PokemonEntity){
        binding.hpValue.text = pokemonEntity.statHp.toString()
        binding.hpBar.progress = pokemonEntity.statHp * 100 / 255
        binding.attackValue.text = pokemonEntity.statAttack.toString()
        binding.attackBar.progress = pokemonEntity.statAttack * 100 / 255
        binding.defenseValue.text = pokemonEntity.statDefense.toString()
        binding.defenseBar.progress = pokemonEntity.statDefense * 100 / 255
        binding.spAtkValue.text = pokemonEntity.statSpAttack.toString()
        binding.spAtkBar.progress = pokemonEntity.statSpAttack * 100 / 255
        binding.spDefValue.text = pokemonEntity.statSpDefense.toString()
        binding.spDefBar.progress = pokemonEntity.statSpDefense * 100 / 255
        binding.speedValue.text = pokemonEntity.statSpeed.toString()
        binding.speedBar.progress = pokemonEntity.statSpeed * 100 / 255

    }

    private fun configureShinyImage(shineButtonState: Boolean){
        if (shineButtonState) {
            binding.shinyButton.setImageResource(R.drawable.ic_shiny_checked)
        }else{
            binding.shinyButton.setImageResource(R.drawable.ic_shiny)
        }
    }

    private fun configureGenderButtons(genderButtonsState: Int) {
        when (genderButtonsState) {
            Constants.GENDERS.MALE -> {
                binding.maleButton.setBackgroundColor(resources.getColor(R.color.male, null))
                binding.maleButton.setColorFilter(resources.getColor(R.color.white, null))
                binding.femaleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.femaleButton.setColorFilter(resources.getColor(R.color.female, null))
            }
            Constants.GENDERS.FEMALE -> {
                binding.maleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.maleButton.setColorFilter(resources.getColor(R.color.male, null))
                binding.femaleButton.setBackgroundColor(resources.getColor(R.color.female, null))
                binding.femaleButton.setColorFilter(resources.getColor(R.color.white, null))
            }
            else -> {
                binding.maleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.maleButton.setColorFilter(resources.getColor(R.color.male, null))
                binding.femaleButton.setBackgroundColor(resources.getColor(R.color.transparent, null))
                binding.femaleButton.setColorFilter(resources.getColor(R.color.female, null))
            }
        }
    }

    private fun configurePokemonImage(shineButtonState: Boolean, genderButtonsState: Int){
        if(shineButtonState && genderButtonsState != Constants.GENDERS.FEMALE)
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgShiny.value)
        else if (shineButtonState && genderButtonsState == Constants.GENDERS.FEMALE)
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgShinyFemale.value)
        else if (!shineButtonState && genderButtonsState != Constants.GENDERS.FEMALE)
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgDefault.value)
        else
            binding.pokemonImage.setImageBitmap(pokemonViewModel.getImgFemale.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}