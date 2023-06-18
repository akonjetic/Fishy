package hr.konjetic.fishy.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import coil.load
import hr.konjetic.fishy.R
import hr.konjetic.fishy.activity.viewmodel.FishActivityViewModel
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.database.entities.FavoriteFishFamily
import hr.konjetic.fishy.database.entities.FavoriteHabitat
import hr.konjetic.fishy.database.entities.FavoriteWaterType
import hr.konjetic.fishy.databinding.ActivityFishBinding
import hr.konjetic.fishy.network.model.Fish

const val EXTRA_FISH = "FISH"
const val EXTRA_FAVORITE = "FAVORITE"

class FishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishBinding
    private val viewModel: FishActivityViewModel by viewModels()

    @SuppressLint("ObsoleteSdkInt")
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

        val sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1)

        binding.fishName.text = "${chosenFish.name} (${chosenFish.gender})"
        binding.favoriteIcon.isActivated = isItFavorite
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.fishDescription.value.text = chosenFish.description
        binding.fishDescription.type.text = getString(R.string.fish_activity_description)
        binding.fishGender.value.text = if (chosenFish.maxNumberOfSameGender > 0){
            chosenFish.maxNumberOfSameGender.toString()
        } else{
            getString(R.string.fish_activity_gender_unlimited)
        }
        binding.fishGender.type.text = getString(R.string.fish_activity_max_num_gender)
        binding.fishMinSchoolSize.value.text =
            "${chosenFish.minSchoolSize} / ${chosenFish.avgSchoolSize}"
        binding.fishMinSchoolSize.type.text = getString(R.string.fish_activity_school_size)
        binding.fishWaterType.value.text = chosenFish.waterType.type
        binding.fishFamily.value.text = chosenFish.fishFamily.name
        binding.fishHabitat.value.text = chosenFish.habitat.name
        binding.fishWaterType.type.text = getString(R.string.fish_activity_water_type)
        binding.fishFamily.type.text = getString(R.string.fish_activity_fish_family)
        binding.fishHabitat.type.text = getString(R.string.fish_activity_habitat)
        binding.fishImage.load(chosenFish.image)

        binding.addToAquarium.setOnClickListener {
            showPopup(chosenFish, userId)
        }


        binding.favoriteIcon.setOnClickListener {
            if (it.isActivated) {
                viewModel.removeFavoriteFishFromDatabaseCascade(
                    this,
                    chosenFish.id.toLong(),
                    chosenFish.habitat.id,
                    chosenFish.waterType.id,
                    chosenFish.fishFamily.id
                )
                it.isActivated = false
            } else {
                it.isActivated = true
                viewModel.insertFavoriteFishToDatabase(
                    this,
                    FavoriteFish(
                        0,
                        userId,
                        chosenFish.id,
                        chosenFish.name,
                        chosenFish.description,
                        FavoriteWaterType(chosenFish.waterType.id, chosenFish.waterType.type),
                        FavoriteFishFamily(chosenFish.fishFamily.id, chosenFish.fishFamily.name),
                        FavoriteHabitat(chosenFish.habitat.id, chosenFish.habitat.name),
                        chosenFish.image,
                        chosenFish.minSchoolSize,
                        chosenFish.avgSchoolSize,
                        chosenFish.gender,
                        chosenFish.maxNumberOfSameGender
                    )
                )
            }
        }

        //status i navigacijski bar boje
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.color_primary)
        }

    }

    private fun showPopup(fish: Fish, userId: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.add_popup_layout, null)

        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
        val quantityET = dialogView.findViewById<EditText>(R.id.quantity)

        var chosenQuantity = 0

        viewModel.getAllAquariumsOfUser(this, userId)

        quantityET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                chosenQuantity = s.toString().toIntOrNull()!!
            }
        })

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayListOf("FIRST", "SECOND", "THIRD")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Select an Aquarium")
        dialogBuilder.setPositiveButton("OK") { _, _ ->

            val aquariumId = when (spinner.selectedItemPosition) {
                0 -> "FIRST"
                1 -> "SECOND"
                2 -> "THIRD"
                else -> null
            }

            val sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", 1)

            if (aquariumId.isNullOrBlank() || chosenQuantity <= 0) {
                Toast.makeText(this, "No Aquarium or Quantity was selected", Toast.LENGTH_SHORT).show()

            } else {
                viewModel.aquariums.observe(this){
                    val chosenAquarium = it.find { aquarium -> aquarium.name == aquariumId  }
                    val fishExists = chosenAquarium?.fish?.any { f -> f.fishId == fish.id }

                    if (fishExists == true ){
                        Toast.makeText(this, "That Fish is already in selected Aquarium", Toast.LENGTH_SHORT).show()
                    } else{
                        viewModel.addFishToAquarium(
                            this,
                            aquariumId,
                            userId,
                            fish.toDBEWithQuantity(chosenQuantity)
                        )
                        Toast.makeText(this, "Added to Aquarium", Toast.LENGTH_SHORT).show()
                    }
                }

            }


        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val b = dialogBuilder.create()
        b.show()
    }
}