package hr.konjetic.fishy.database

import androidx.room.*
import hr.konjetic.fishy.database.entities.Aquarium
import hr.konjetic.fishy.database.entities.FavoriteFish

@Dao
interface FishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFish(favoriteFish : FavoriteFish)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAquarium(aquarium : Aquarium)

    @Delete
    suspend fun deleteFavoriteFish(favoriteFish: FavoriteFish)

    @Query("DELETE FROM Aquarium WHERE aquariumUserId = :id")
    suspend fun deleteAquariumsByUser(id: Int)

    @Query("DELETE FROM FavoriteFish WHERE fishId = :id")
    suspend fun deleteFavoriteFishById(id: Long)

    @Query("DELETE FROM FavoriteFish WHERE userId = :id")
    suspend fun deleteFavoriteFishByUserId(id: Int)

    @Query("DELETE FROM FavoriteHabitat WHERE habitatId = :id")
    suspend fun deleteFavoriteHabitatById(id: Int)

    @Query("DELETE FROM favoritewatertype WHERE waterTypeId = :id")
    suspend fun deleteFavoriteWaterTypeById(id: Int)

    @Query("DELETE FROM FavoriteFishFamily WHERE fishFamilyId = :id")
    suspend fun deleteFavoriteFishFamilyById(id: Int)

    @Query("SELECT * FROM Aquarium WHERE aquariumUserId = :userId")
    suspend fun getAllAquariumsOfUser(userId: Int) : List<Aquarium>

    @Query("SELECT * FROM Aquarium WHERE aquariumUserId = :userId AND aquariumName = :aquariumId")
    suspend fun getAquariumsOfUserByAquariumId(userId: Int, aquariumId : String) : Aquarium

    @Query("SELECT * FROM FavoriteFish WHERE userId = :userId")
    suspend fun getFavoriteFishByUser(userId: Int) : List<FavoriteFish>

    @Query("SELECT * FROM FavoriteFish WHERE userId = :userId and fishId = :fishId")
    suspend fun getFavoriteFishByUserAndFish(userId: Int, fishId: Int) : List<FavoriteFish>

    @Update
    suspend fun updateAquarium(aquarium: Aquarium)

    @Transaction
    suspend fun deleteFavoriteFishOfUserWithCascade(userId: Int){
        val fish = getFavoriteFishByUser(userId)
        deleteFavoriteFishByUserId(userId)
        for (fishy in fish){
            deleteFavoriteHabitatById(fishy.habitat.id)
            deleteFavoriteFishFamilyById(fishy.fishFamily.id)
            deleteFavoriteWaterTypeById(fishy.waterType.id)
        }
    }

    @Transaction
    suspend fun deleteFavoriteFishWithCascade(favFishId : Long, waterTypeId : Int, habitatId : Int, fishFamilyId : Int){
        deleteFavoriteFishById(favFishId)
        deleteFavoriteHabitatById(habitatId)
        deleteFavoriteFishFamilyById(fishFamilyId)
        deleteFavoriteWaterTypeById(waterTypeId)
    }
}