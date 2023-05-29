package hr.konjetic.fishy.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.konjetic.fishy.network.Network
import hr.konjetic.fishy.network.model.*
import kotlinx.coroutines.launch

@Suppress("BlockingMethodInNonBlockingContext")
class AdminActivityViewModel : ViewModel() {

    val listOfFish = MutableLiveData<ArrayList<Fish>>()
    val listOfFishFamilies = MutableLiveData<ArrayList<FishFamily>>()
    val listOfHabitats = MutableLiveData<ArrayList<Habitat>>()
    val listOfWaterTypes = MutableLiveData<ArrayList<WaterType>>()
    val fish = MutableLiveData<Fish>()
    val newFishId = MutableLiveData<String>()

    fun getAllFish(){
        viewModelScope.launch {
            listOfFish.value = Network().getService().getAllFish() as ArrayList<Fish>
        }
    }

    fun getAllFishFamilies(){
        viewModelScope.launch {
            listOfFishFamilies.value = Network().getService().getAllFishFamilies() as ArrayList<FishFamily>
        }
    }

    fun getAllHabitats(){
        viewModelScope.launch {
            listOfHabitats.value = Network().getService().getAllHabitats() as ArrayList<Habitat>
        }
    }

    fun getAllWaterTypes(){
        viewModelScope.launch {
            listOfWaterTypes.value = Network().getService().getAllWaterTypes() as ArrayList<WaterType>
        }
    }

    fun createNewFish(data: FishDTO){
        viewModelScope.launch {
          val postFish = Network().getService().postFish(data)

            if (postFish.isSuccessful){
                newFishId.value = postFish.body()?.string()
            }
        }
    }

    fun getFishByName(name: String){
        viewModelScope.launch {
            val response = Network().getService().getFishByName(name)

            if (response.isSuccessful){
                fish.value = response.body()
            }
        }
    }
}