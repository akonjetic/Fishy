package hr.konjetic.fishy.activity.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.database.entities.Aquarium
import hr.konjetic.fishy.database.entities.AquariumFish
import hr.konjetic.fishy.database.entities.FavoriteFish
import kotlinx.coroutines.launch

class FishActivityViewModel : ViewModel() {

    val aquariums = MutableLiveData<ArrayList<Aquarium>>()
    val aquarium = MutableLiveData<Aquarium>()

    fun getAllAquariumsOfUser(context: Context, userId: Int) {
        viewModelScope.launch {
            aquariums.value = FishDatabase.getDatabase(context)?.getFishDao()
                ?.getAllAquariumsOfUser(userId) as ArrayList<Aquarium>
        }

    }

    fun addFishToAquarium(context: Context, aquariumId: String, userId: Int, fish: AquariumFish) {
        viewModelScope.launch {
            val aquariumsOfUser = FishDatabase.getDatabase(context)?.getFishDao()
                ?.getAllAquariumsOfUser(userId) as ArrayList<Aquarium>

            when {
                aquariumsOfUser.isNullOrEmpty() -> {
                    FishDatabase.getDatabase(context)?.getFishDao()?.insertAquarium(
                        Aquarium(
                            id = 0,
                            userId = userId,
                            name = aquariumId,
                            fish = arrayListOf(fish),
                            rating = null
                        )
                    )
                }
                aquariumsOfUser.filter { a -> a.name == aquariumId }.isNullOrEmpty() -> {
                    FishDatabase.getDatabase(context)?.getFishDao()?.insertAquarium(
                        Aquarium(
                            id = 0,
                            userId = userId,
                            name = aquariumId,
                            fish = arrayListOf(fish),
                            rating = null
                        )
                    )
                }
                else -> {
                    val aquarium = aquariumsOfUser.firstOrNull { a -> a.name == aquariumId }
                    aquarium?.fish?.add(fish)
                    FishDatabase.getDatabase(context)?.getFishDao()?.updateAquarium(aquarium!!)
                }
            }
        }

    }


    fun insertFavoriteFishToDatabase(context: Context, favoriteFish: FavoriteFish) {
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.insertFavoriteFish(favoriteFish)
        }
    }

    fun removeFavoriteFishFromDatabaseCascade(
        context: Context,
        id: Long,
        habitatId: Int,
        waterTypeId: Int,
        fishFamilyId: Int
    ) {
        viewModelScope.launch {
            FishDatabase.getDatabase(context)?.getFishDao()?.deleteFavoriteFishWithCascade(
                favFishId = id,
                habitatId = habitatId,
                waterTypeId = waterTypeId,
                fishFamilyId = fishFamilyId
            )
        }
    }

}