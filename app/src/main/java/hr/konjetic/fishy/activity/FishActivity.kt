package hr.konjetic.fishy.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import coil.load
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.AdminActivityViewModel
import hr.konjetic.fishy.activity.viewmodel.FishActivityViewModel
import hr.konjetic.fishy.databinding.ActivityFishBinding
import hr.konjetic.fishy.network.model.Fish

const val EXTRA_FISH = "FISH"
const val EXTRA_FAVORITE = "FAVORITE"

class FishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishBinding
    private val viewModel : FishActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFishBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val chosenFish = intent?.extras?.getSerializable(EXTRA_FISH) as Fish
        val isItFavorite = intent.getBooleanExtra(EXTRA_FAVORITE, false)

        //skrivanje action bara u dark modeu i prislini light mode
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /*setSupportActionBar(binding.toolbar)
        binding.fishName.text = chosenFish.name*/
        binding.fishName.text = chosenFish.name
        binding.favoriteIcon.isActivated = isItFavorite
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.fishDescription.value.text = chosenFish.description
        binding.fishDescription.type.text = "DESCRIPTION"
        binding.fishGender.value.text = chosenFish.gender
        binding.fishMaxNumOfSameGender.value.text = chosenFish.maxNumberOfSameGender.toString()
        binding.fishGender.type.text = "GENDER"
        binding.fishMaxNumOfSameGender.type.text = "MAX NUMBER OF SAME GENDER"
        binding.fishMinAquariumSizeL.value.text = chosenFish.MinAquariumSizeInL.toString()
        binding.fishStoreQuantity.value.text = chosenFish.availableInStore.toString()
        binding.fishMinAquariumSizeL.type.text = "MIN AQUARIUM SIZE IN L"
        binding.fishStoreQuantity.type.text = "AVAILABLE QUANTITY"
        binding.fishMinSchoolSize.value.text = chosenFish.minSchoolSize.toString()
        binding.fishAvgSchoolSize.value.text = chosenFish.avgSchoolSize.toString()
        binding.fishMinSchoolSize.type.text = "MIN SCHOOL SIZE"
        binding.fishAvgSchoolSize.type.text = "AVERAGE SCHOOL SIZE"
        binding.fishWaterType.value.text = chosenFish.waterType.type
        binding.fishFamily.value.text = chosenFish.fishFamily.name
        binding.fishHabitat.value.text = chosenFish.habitat.name
        binding.fishWaterType.type.text = "WATER TYPE"
        binding.fishFamily.type.text = "FISH FAMILY"
        binding.fishHabitat.type.text = "HABITAT"
        binding.fishImage.load(chosenFish.image)

        binding.addToAquarium.setOnClickListener {
            showPopup(chosenFish)
        }


    }

    private fun showPopup(fish : Fish){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.add_popup_layout, null)

        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
        val quantityET = dialogView.findViewById<EditText>(R.id.quantity)

        val maxQuantity = fish.availableInStore
        var chosenQuantity : Int = 0

        quantityET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val inputNumber = s.toString().toIntOrNull()
                if (inputNumber != null && inputNumber > maxQuantity) {
                    quantityET.setText(maxQuantity.toString())
                    quantityET.setSelection(quantityET.text.length)
                    chosenQuantity = maxQuantity
                } else{
                    chosenQuantity = s.toString().toIntOrNull()!!
                }


            }
        })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayListOf("FIRST", "SECOND", "THIRD"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Select an Aquarium")
        dialogBuilder.setPositiveButton("OK") { dialog, whichButton ->
            val selectedAquarium = spinner.selectedItemPosition

            val aquariumId = when(selectedAquarium){
                0 -> "FIRST"
                1 -> "SECOND"
                2 -> "THIRD"
                else -> null
            }

            val sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 1)

            if (aquariumId.isNullOrBlank()){
                Toast.makeText(this, "No Aquarium was Chosen", Toast.LENGTH_SHORT).show()

            } else{




                viewModel.updateFishInApi(Fish(fish.id, fish.name, fish.description, fish.waterType, fish.fishFamily, fish.habitat, fish.image, fish.minSchoolSize, fish.avgSchoolSize, fish.MinAquariumSizeInL, fish.gender, fish.maxNumberOfSameGender, fish.availableInStore.minus(chosenQuantity)))

                viewModel.addFishToAquarium(this, aquariumId, userId, fish.toDBEWithQuantity(chosenQuantity))
                Toast.makeText(this, "Added to Aquarium", Toast.LENGTH_SHORT).show()
            }



        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
            dialog.dismiss()
        }

        val b = dialogBuilder.create()
        b.show()
    }
}