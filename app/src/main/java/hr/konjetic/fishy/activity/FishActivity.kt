package hr.konjetic.fishy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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

       /* binding.addToAquarium.setOnClickListener {
            viewModel.addFishToAquarium(this, )
        }*/
    }
}