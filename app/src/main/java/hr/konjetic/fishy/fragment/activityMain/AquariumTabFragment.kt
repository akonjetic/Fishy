package hr.konjetic.fishy.fragment.activityMain

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.MainActivityViewModel
import hr.konjetic.fishy.adapter.AquariumTabFragmentAdapter
import hr.konjetic.fishy.adapter.FavoritesFragmentAdapter
import hr.konjetic.fishy.database.entities.AquariumFish
import hr.konjetic.fishy.databinding.FragmentAquariumTabBinding


class AquariumTabFragment(private val tabPosition: Int) : Fragment() {

    private var _binding : FragmentAquariumTabBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()

    private val aquariumAdapter by lazy { AquariumTabFragmentAdapter(requireContext(), arrayListOf()) }

    private var reasonsForNegativeRatings : ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAquariumTabBinding.inflate(inflater, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        viewModel.getAllAquariumsOfUserById(requireContext(), userId)
        viewModel.getAllFishFamilyCompatibleData()
        viewModel.getAllFishFamilyIncompatibleData()

        binding.recyclerAquarium.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAquarium.adapter = aquariumAdapter

        when(tabPosition){
            0 -> {

                viewModel.aquarium1.observe(viewLifecycleOwner){
                    if (it == null){
                        null
                    } else {
                        aquariumAdapter.updateFish(it.fish)
                        val rating = calculateRating(it.fish)
                        println("########rating 1" + rating)
                    }
                }
            }
            1 -> {
                viewModel.aquarium2.observe(viewLifecycleOwner){
                    if (it == null){
                        null
                    } else {
                        aquariumAdapter.updateFish(it.fish)
                        val rating = calculateRating(it.fish)
                        println("########rating 2" + rating)
                    }
                }
            }
            2 -> {
                viewModel.aquarium3.observe(viewLifecycleOwner){
                    if (it == null){
                        null
                    } else{
                        aquariumAdapter.updateFish(it.fish)
                        val rating = calculateRating(it.fish)
                        println("########rating 3" + rating)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        viewModel.getAllAquariumsOfUserById(requireContext(), userId)
    }

    fun calculateRating(fish: ArrayList<AquariumFish>) : Double{
        var fishFamilyCompatibility = 5.0
        var sameGender = 3.0
        var schoolSize = 2.0
        var isIncompatible = false
        var genderIncompatibility = false
        var schoolSizeIncompatibility = false



        val listOfFishLength = fish.size

        viewModel.incompatibleList.observe(viewLifecycleOwner){
            for (fishFamilyCombo in it){
                val parentId = fishFamilyCombo.parentId
                val incompatibleWith = fishFamilyCombo.compatibilityId

                val isParentInAquarium = fish.any { f -> f.fishFamily.id == parentId }
                val isIncompatibleInAquarium = fish.any { f -> f.fishFamily.id == incompatibleWith }

                if (isParentInAquarium && isIncompatibleInAquarium){
                    isIncompatible = true
                    fishFamilyCompatibility -= 0.5
                }
            }
        }

        for (fishy in fish){
            if (fishy.maxNumberOfSameGender > fishy.availableInStore && fishy.maxNumberOfSameGender > 0){
                sameGender -= (0.1*(fishy.availableInStore - fishy.maxNumberOfSameGender))
                genderIncompatibility = true
            }

            val matchingFishSpecies = fish.find { it.name == fishy.name && it.gender != fishy.gender }
            if (matchingFishSpecies == null){
                if (fishy.minSchoolSize < fishy.availableInStore){
                    schoolSize -= 0.2
                    schoolSizeIncompatibility = true
                }
            } else{
                if (fishy.minSchoolSize < (fishy.availableInStore + matchingFishSpecies.availableInStore)){
                    schoolSize -= 0.2
                    schoolSizeIncompatibility = true
                }
            }
        }

        reasonsForNegativeRatings.clear()

        if (genderIncompatibility){
            reasonsForNegativeRatings.add("Incompatible numbers of species of same gender")
        }
        if (isIncompatible){
            reasonsForNegativeRatings.add("Incompatible fish family combinations")
        }
        if (schoolSizeIncompatibility){
            reasonsForNegativeRatings.add("School sizes of species were below minimum")
        }


        return sameGender+fishFamilyCompatibility+schoolSize
    }

}