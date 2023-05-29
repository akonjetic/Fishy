package hr.konjetic.fishy.activity.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.konjetic.fishy.database.FishDatabase
import hr.konjetic.fishy.database.entities.Aquarium
import hr.konjetic.fishy.database.entities.AquariumFish
import hr.konjetic.fishy.database.entities.FavoriteFish
import hr.konjetic.fishy.network.model.Fish
import kotlinx.coroutines.launch

class FishActivityViewModel : ViewModel(){

    val aquariums = MutableLiveData<ArrayList<Aquarium>>()

    fun getAllAquariumsOfUser(context: Context, userId : Int) {
        viewModelScope.launch {
            aquariums.value = FishDatabase.getDatabase(context)?.getFishDao()
                ?.getAllAquariumsOfUser(userId) as ArrayList<Aquarium>
        }

    }

    fun addFishToAquarium(context: Context, aquariumId : String, userId: Int, fish: AquariumFish) {
        viewModelScope.launch {
            val aquariumsOfUser = FishDatabase.getDatabase(context)?.getFishDao()
                ?.getAllAquariumsOfUser(userId) as ArrayList<Aquarium>

            if (aquariumsOfUser.isNullOrEmpty()){
                FishDatabase.getDatabase(context)?.getFishDao()?.insertAquarium(
                    Aquarium(id = 0, userId = userId, name = aquariumId, fish = arrayListOf(fish), rating = null)
                )
            } else if(aquariumsOfUser.filter { a -> a.name == aquariumId }.isNullOrEmpty()){
                FishDatabase.getDatabase(context)?.getFishDao()?.insertAquarium(
                    Aquarium(id = 0, userId = userId, name = aquariumId, fish = arrayListOf(fish), rating = null)
                )
            } else{
                val aquarium = aquariumsOfUser.firstOrNull { a -> a.name == aquariumId }
                aquarium?.fish?.add(fish)
                FishDatabase.getDatabase(context)?.getFishDao()?.updateAquarium(aquarium!!)
            }
        }

    }

    fun removeFishFromAquarium(context: Context, aquariumId: String, userId: Int, fish: AquariumFish){
        viewModelScope.launch {
            val aquarium = FishDatabase.getDatabase(context)?.getFishDao()?.getAquariumsOfUserByAquariumId(userId, aquariumId)
            aquarium?.fish?.remove(fish)
            FishDatabase.getDatabase(context)?.getFishDao()?.updateAquarium(aquarium!!)
        }
    }

}