package hr.konjetic.fishy.activity.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.network.Network
import hr.konjetic.fishy.network.model.Fish
import hr.konjetic.fishy.network.model.FishFamily
import hr.konjetic.fishy.network.model.Habitat
import hr.konjetic.fishy.network.model.WaterType
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val listOfFish = MutableLiveData<ArrayList<Fish>>()
    val listOfFishFamilies = MutableLiveData<ArrayList<FishFamily>>()
    val listOfHabitats = MutableLiveData<ArrayList<Habitat>>()
    val listOfWaterTypes = MutableLiveData<ArrayList<WaterType>>()
    val listOfFavoriteFish = MutableLiveData<ArrayList<FavoriteFish>>()

    fun getAllFish() {
        viewModelScope.launch {
            listOfFish.value = Network().getService().getAllFish() as ArrayList<Fish>
        }
    }

    fun getAllFishFamilies() {
        viewModelScope.launch {
            listOfFishFamilies.value =
                Network().getService().getAllFishFamilies() as ArrayList<FishFamily>
        }
    }

    fun getAllHabitats() {
        viewModelScope.launch {
            listOfHabitats.value = Network().getService().getAllHabitats() as ArrayList<Habitat>
        }
    }

    fun getAllWaterTypes() {
        viewModelScope.launch {
            listOfWaterTypes.value =
                Network().getService().getAllWaterTypes() as ArrayList<WaterType>
        }
    }

    fun getFavoriteFish(context: Context, userId: Int) {
        viewModelScope.launch {
            listOfFavoriteFish.value = FishDatabase.getDatabase(context)?.getFishDao()
                ?.getFavoriteFishByUser(userId) as ArrayList<FavoriteFish>
        }

    }

    fun insertFavoriteFishToDatabase(context: Context, favoriteFish: FavoriteFish){
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.insertFavoriteFish(favoriteFish)
        }
    }

    fun removeFavoriteFishFromDatabase(context: Context, id : Long){
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.deleteFavoriteFishById(id)
        }
    }

    fun removeFavoriteFishFromDatabaseCascade(context: Context, id : Long, habitatId: Int, waterTypeId : Int, fishFamilyId: Int){
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.deleteFavoriteFishWithCascade(favFishId = id, habitatId = habitatId, waterTypeId = waterTypeId, fishFamilyId = fishFamilyId)
        }
    }
}