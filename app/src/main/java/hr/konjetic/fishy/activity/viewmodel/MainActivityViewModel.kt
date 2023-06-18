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
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val listOfFavoriteFish = MutableLiveData<ArrayList<FavoriteFish>>()
    val scannedFish = MutableLiveData<Fish>()
    val aquarium1 = MutableLiveData<Aquarium>()
    val aquarium2 = MutableLiveData<Aquarium>()
    val aquarium3 = MutableLiveData<Aquarium>()
    val incompatibleList = MutableLiveData<ArrayList<FishFamilyCompatibility>>()
    var fishSearchResult = MutableLiveData<ArrayList<Fish>>()

    fun refreshFishFlow(): Flow<PagingData<Fish>> {
        val pager = Pager(PagingConfig(pageSize = 5)) {
            FishPagingSource(Network().getService())
        }
        return pager.flow.cachedIn(viewModelScope)
    }


    fun getFishById(id: String) {
        viewModelScope.launch {
            val response = Network().getService().getFishById(id.toInt())

            if (response.isSuccessful) {
                scannedFish.value = response.body()
            }
        }
    }

    fun getFavoriteFish(context: Context, userId: Int) {
        viewModelScope.launch {
            listOfFavoriteFish.value = FishDatabase.getDatabase(context)?.getFishDao()
                ?.getFavoriteFishByUser(userId) as ArrayList<FavoriteFish>
        }

    }


    fun getAllAquariumsOfUserById(context: Context, userId: Int) {
        viewModelScope.launch {
            aquarium1.value =
                FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                    userId, "FIRST"
                )
            aquarium2.value =
                FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                    userId, "SECOND"
                )
            aquarium3.value =
                FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(
                    userId, "THIRD"
                )
        }
    }

    fun deleteAquariumsOfUser(context: Context, userId: Int) {
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.deleteAquariumsByUser(userId)
        }
    }

    fun removeFavoriteFishOfUserFromDatabaseCascade(context: Context, id: Int) {
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()
                ?.deleteFavoriteFishOfUserWithCascade(userId = id)
        }
    }

    fun getAllFishFamilyIncompatibleData() {
        viewModelScope.launch {
            incompatibleList.value = Network().getService().getAllIncompatibleData()
        }
    }

    fun getFishByName(name: String) {
        viewModelScope.launch {
            fishSearchResult.value = Network().getService().getFishByName(name)
        }
    }
}