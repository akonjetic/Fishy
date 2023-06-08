package hr.konjetic.fishy.activity.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.database.entities.Aquarium
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.network.Network
import hr.konjetic.fishy.network.model.*
import hr.konjetic.fishy.network.paging.FishPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val listOfFish = MutableLiveData<ArrayList<Fish>>()
    val listOfFishFamilies = MutableLiveData<ArrayList<FishFamily>>()
    val listOfHabitats = MutableLiveData<ArrayList<Habitat>>()
    val listOfWaterTypes = MutableLiveData<ArrayList<WaterType>>()
    val listOfFavoriteFish = MutableLiveData<ArrayList<FavoriteFish>>()
    val scannedFish = MutableLiveData<Fish>()
    val aquarium1 = MutableLiveData<Aquarium>()
    val aquarium2 = MutableLiveData<Aquarium>()
    val aquarium3 = MutableLiveData<Aquarium>()
    val compatibleList = MutableLiveData<ArrayList<FishFamilyCompatibility>>()
    val incompatibleList = MutableLiveData<ArrayList<FishFamilyCompatibility>>()
    var fishSearchResult = MutableLiveData<ArrayList<Fish>>()

    fun getAllFish() {
        viewModelScope.launch {
            listOfFish.value = Network().getService().getAllFish() as ArrayList<Fish>
        }
    }

   /*val fishFlow = Pager(PagingConfig(pageSize = 5)){
        FishPagingSource(Network().getService())
    }.flow.cachedIn(viewModelScope)*/


    fun refreshFishFlow(): Flow<PagingData<Fish>> {
        val pager = Pager(PagingConfig(pageSize = 5)) {
            FishPagingSource(Network().getService())
        }
        return pager.flow.cachedIn(viewModelScope)
    }


    fun getFishById(id: String){
        viewModelScope.launch {
            val response = Network().getService().getFishById(id.toInt())

            if (response.isSuccessful){
                scannedFish.value = response.body()
            }
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

   /* fun getAquariumOfUserById(context: Context, aquariumId: String, userId: Int){
        viewModelScope.launch {
            aquarium.value = FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                userId, aquariumId
            )
        }
    }*/

    fun getAllAquariumsOfUserById(context: Context, userId: Int){
        viewModelScope.launch {
            aquarium1.value = FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                userId, "FIRST"
            )
            aquarium2.value = FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                userId, "SECOND"
            )
            aquarium3.value = FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                userId, "THIRD"
            )
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

    fun removeFavoriteFishOfUserFromDatabaseCascade(context: Context, id : Int){
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.deleteFavoriteFishOfUserWithCascade(userId = id)
        }
    }

    fun getAllFishFamilyCompatibleData(){
        viewModelScope.launch {
            compatibleList.value = Network().getService().getAllCompatibleData()
        }
    }

    fun getAllFishFamilyIncompatibleData(){
        viewModelScope.launch {
            incompatibleList.value = Network().getService().getAllIncompatibleData()
        }
    }

    fun getFishByName(name: String){
        viewModelScope.launch {
            fishSearchResult.value = Network().getService().getFishByName(name)
        }
    }
}