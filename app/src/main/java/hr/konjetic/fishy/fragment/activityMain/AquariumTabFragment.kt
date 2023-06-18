package hr.konjetic.fishy.fragment.activityMain

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.MainActivityViewModel
import hr.konjetic.fishy.adapter.AquariumTabFragmentAdapter
import hr.konjetic.fishy.database.entities.Aquarium
import hr.konjetic.fishy.database.entities.AquariumFish
import hr.konjetic.fishy.databinding.FragmentAquariumTabBinding
import kotlin.collections.ArrayList


class AquariumTabFragment() : Fragment() {

    private var _binding: FragmentAquariumTabBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()

    private val aquariumAdapter by lazy {
        AquariumTabFragmentAdapter(
            requireContext(),
            arrayListOf(),
            Aquarium(0, 0, "", 0.0, arrayListOf())
        )
    }

    private var reasonsForNegativeRatings: ArrayList<String> = arrayListOf()

    private val fishFamilyCompatibilityStart = 5.0
    private val sameGenderStart = 3.0
    private val schoolSizeStart = 2.0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAquariumTabBinding.inflate(inflater, container, false)

        val sharedPreferences =
            requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        viewModel.getAllFishFamilyIncompatibleData()
        viewModel.getAllAquariumsOfUserById(requireContext(), userId)


        binding.recyclerAquarium.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAquarium.adapter = aquariumAdapter


        when (val tabPosition = arguments?.getInt("position", 0) ?: 0) {
            0 -> {
                var editable = false
                val itemTouchHelper = ItemTouchHelper(ItemTouchHelperFish)
                binding.ratingHeader.editIcon.setOnClickListener {

                    if (!editable) {
                        editable = true
                        binding.ratingHeader.editIcon.isActivated = editable
                        aquariumAdapter.updateEditable()
                        itemTouchHelper.attachToRecyclerView(binding.recyclerAquarium)
                    } else{
                        editable = false
                        binding.ratingHeader.editIcon.isActivated = editable
                        aquariumAdapter.updateEditable()
                        viewModel.getAllAquariumsOfUserById(requireContext(), userId)
                        itemTouchHelper.attachToRecyclerView(null)
                    }
                }

                binding.placeholder.visibility = View.VISIBLE

                viewModel.aquarium1.observe(viewLifecycleOwner) {

                    when {
                        it == null -> {
                            binding.placeholder.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                        }
                        it.fish.isNullOrEmpty() -> {
                            binding.placeholder.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                            val rating = calculateRating(it.fish)
                            binding.ratingHeader.ratingText.text =
                                "Rating: ${String.format("%.2f", rating)}"
                        }
                        else -> {
                            binding.placeholder.visibility = View.GONE
                            binding.recyclerLayout.visibility = View.VISIBLE
                            aquariumAdapter.updateFishAndAquarium(it.fish, it)
                            val rating = calculateRating(it.fish)
                            binding.ratingHeader.ratingText.text =
                                "Rating: ${String.format("%.2f", rating)}"
                            setRatingBackgroundColor(binding.ratingHeader.ratingText, rating)

                        }
                    }
                }
            }
            1 -> {
                var editable = false
                val itemTouchHelper = ItemTouchHelper(ItemTouchHelperFish)
                binding.ratingHeader.editIcon.setOnClickListener {

                    if (!editable) {
                        editable = true
                        binding.ratingHeader.editIcon.isActivated = editable
                        aquariumAdapter.updateEditable()
                        itemTouchHelper.attachToRecyclerView(binding.recyclerAquarium)
                    } else{
                        editable = false
                        binding.ratingHeader.editIcon.isActivated = editable
                        aquariumAdapter.updateEditable()
                        viewModel.getAllAquariumsOfUserById(requireContext(), userId)
                        itemTouchHelper.attachToRecyclerView(null)
                    }
                }

                binding.placeholder.visibility = View.VISIBLE
                binding.recyclerLayout.visibility = View.GONE

                viewModel.aquarium2.observe(viewLifecycleOwner) {
                    when {
                        it == null -> {
                            binding.placeholder.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                        }
                        it.fish.isNullOrEmpty() -> {
                            binding.placeholder.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                            val rating = calculateRating(it.fish)
                            binding.ratingHeader.ratingText.text =
                                "Rating: ${String.format("%.2f", rating)}"
                        }
                        else -> {
                            binding.placeholder.visibility = View.GONE
                            binding.recyclerLayout.visibility = View.VISIBLE
                            aquariumAdapter.updateFishAndAquarium(it.fish, it)
                            val rating = calculateRating(it.fish)
                            binding.ratingHeader.ratingText.text =
                                "Rating: ${String.format("%.2f", rating)}"
                            setRatingBackgroundColor(binding.ratingHeader.ratingText, rating)
                        }
                    }
                }
            }
            2 -> {
                var editable = false
                val itemTouchHelper = ItemTouchHelper(ItemTouchHelperFish)
                binding.ratingHeader.editIcon.setOnClickListener {

                    if (!editable) {
                        editable = true
                        binding.ratingHeader.editIcon.isActivated = editable
                        aquariumAdapter.updateEditable()
                        itemTouchHelper.attachToRecyclerView(binding.recyclerAquarium)
                    } else{
                        editable = false
                        binding.ratingHeader.editIcon.isActivated = editable
                        aquariumAdapter.updateEditable()
                        viewModel.getAllAquariumsOfUserById(requireContext(), userId)
                        itemTouchHelper.attachToRecyclerView(null)
                    }
                }

                binding.placeholder.visibility = View.VISIBLE
                binding.recyclerLayout.visibility = View.GONE


                viewModel.aquarium3.observe(viewLifecycleOwner) {
                    when {
                        it == null -> {
                            binding.placeholder.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                        }
                        it.fish.isNullOrEmpty() -> {
                            binding.placeholder.visibility = View.VISIBLE
                            binding.recyclerLayout.visibility = View.GONE
                            val rating = calculateRating(it.fish)
                            binding.ratingHeader.ratingText.text =
                                "Rating: ${String.format("%.2f", rating)}"
                        }
                        else -> {
                            binding.placeholder.visibility = View.GONE
                            binding.recyclerLayout.visibility = View.VISIBLE
                            aquariumAdapter.updateFishAndAquarium(it.fish, it)
                            val rating = calculateRating(it.fish)
                            binding.ratingHeader.ratingText.text =
                                "Rating: ${String.format("%.2f", rating)}"
                            setRatingBackgroundColor(binding.ratingHeader.ratingText, rating)
                        }
                    }
                }
            }


        }

        binding.ratingHeader.infoImg.setOnClickListener {
            showPopup()
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences =
            requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        viewModel.getAllFishFamilyIncompatibleData()
        viewModel.getAllAquariumsOfUserById(requireContext(), userId)

    }

    private fun calculateRating(fish: ArrayList<AquariumFish>): Double {
        var isIncompatible = false
        var genderIncompatibility = false
        var schoolSizeIncompatibility = false

        var fishFamilyCompatibility = fishFamilyCompatibilityStart
        var sameGender = sameGenderStart
        var schoolSize = schoolSizeStart


        viewModel.incompatibleList.observe(viewLifecycleOwner) {
            for (fishFamilyCombo in it) {
                val parentId = fishFamilyCombo.parentId
                val incompatibleWith = fishFamilyCombo.compatibilityId

                val isParentInAquarium = fish.any { f -> f.fishFamily.id == parentId }
                val isIncompatibleInAquarium = fish.any { f -> f.fishFamily.id == incompatibleWith }

                if (isParentInAquarium && isIncompatibleInAquarium) {
                    isIncompatible = true
                    fishFamilyCompatibility -= 0.5
                }
            }
        }

        fish.forEach { fishy ->
            val anyOppositeWaterTypes = fish.any { f -> f.waterType.id != fishy.waterType.id }

            if (anyOppositeWaterTypes) {
                reasonsForNegativeRatings.clear()
                reasonsForNegativeRatings.add("Incompatible water types!")

                return 0.00
            }

                if (fishy.maxNumberOfSameGender < fishy.quantity && fishy.maxNumberOfSameGender > 0) {
                    sameGender -= (0.1 * (fishy.quantity - fishy.maxNumberOfSameGender))
                    genderIncompatibility = true
                }

                val matchingFishSpecies = fish.find {
                    it.name == fishy.name && it.gender != fishy.gender && it.fishFamily == fishy.fishFamily
                }

                if (matchingFishSpecies != null) {
                    val totalQuantity = fishy.quantity + matchingFishSpecies.quantity
                    if (fishy.minSchoolSize > totalQuantity) {
                        schoolSize -= 0.2
                        schoolSizeIncompatibility = true
                    }
                } else {
                    if (fishy.minSchoolSize > fishy.quantity) {
                        println("min size: ${fishy.minSchoolSize}, quantity: ${fishy.quantity}")
                        schoolSize -= 0.2
                        schoolSizeIncompatibility = true
                    }
                }
            }

            reasonsForNegativeRatings.clear()

            if (genderIncompatibility) {
                reasonsForNegativeRatings.add("Incompatible numbers of species of same gender")
            }
            if (isIncompatible) {
                reasonsForNegativeRatings.add("Incompatible fish family combinations")
            }
            if (schoolSizeIncompatibility) {
                reasonsForNegativeRatings.add("School sizes of species were below minimum")
            }
            if (reasonsForNegativeRatings.isEmpty()) {
                reasonsForNegativeRatings.add("You've got the perfect fish combination!")
            }


            return sameGender + fishFamilyCompatibility + schoolSize

    }

    private fun showPopup() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.info_popup_layout, null)

        val reasonsText = dialogView.findViewById<TextView>(R.id.infoText)
        var reasons = String()

        for (reason in reasonsForNegativeRatings) {
            reasons += (reason + "\n")
        }

        reasonsText.text = reasons

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Rating explanation:")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()

        }
        val b = dialogBuilder.create()
        b.show()
    }

    private val ItemTouchHelperFish = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            aquariumAdapter.removeItem(position)

            val sharedPreferences = requireContext().getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 1)
            viewModel.getAllAquariumsOfUserById(requireContext(), userId)
        }
    }

    private fun setRatingBackgroundColor(textView: TextView, number: Double) {
        val context = textView.context
        val backgroundColor = when {
            number > 9.5 -> ContextCompat.getColor(context, R.color.light_green)
            number > 7.5 && number <= 9.5 -> ContextCompat.getColor(context, R.color.color_secondary)
            else -> ContextCompat.getColor(context, R.color.light_red)
        }
        textView.setBackgroundColor(backgroundColor)
    }
}