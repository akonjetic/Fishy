package hr.konjetic.fishy.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}