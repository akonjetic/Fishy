package hr.konjetic.fishy.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hr.konjetic.fishy.network.Network
import hr.konjetic.fishy.network.model.*
import hr.konjetic.fishy.network.paging.FishPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Suppress("BlockingMethodInNonBlockingContext")
class AdminActivityViewModel : ViewModel() {

    val listOfFishFamilies = MutableLiveData<ArrayList<FishFamily>>()
    val listOfHabitats = MutableLiveData<ArrayList<Habitat>>()
    val listOfWaterTypes = MutableLiveData<ArrayList<WaterType>>()
    val fish = MutableLiveData<Fish>()
    val newFishId = MutableLiveData<String>()


    fun refreshFishFlow(): Flow<PagingData<Fish>> {
        val pager = Pager(PagingConfig(pageSize = 5)) {
            FishPagingSource(Network().getService())
        }
        return pager.flow.cachedIn(viewModelScope)
    }

    fun getAllFishFamilies(){
        viewModelScope.launch {
            listOfFishFamilies.value = Network().getService().getAllFishFamilies()
        }
    }

    fun getAllHabitats(){
        viewModelScope.launch {
            listOfHabitats.value = Network().getService().getAllHabitats()
        }
    }

    fun getAllWaterTypes(){
        viewModelScope.launch {
            listOfWaterTypes.value = Network().getService().getAllWaterTypes()
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

    fun deleteFish(id: Int){
        viewModelScope.launch {
            Network().getService().deleteFishById(id)
        }
    }

}