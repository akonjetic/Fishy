package hr.konjetic.fishy.database

import androidx.room.*
import hr.konjetic.fishy.database.entities.FavoriteFish

@Dao
interface FishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFish(favoriteFish : FavoriteFish)

    @Delete
    suspend fun deleteFavoriteFish(favoriteFish: FavoriteFish)

    @Query("DELETE FROM FavoriteFish WHERE id = :id")
    suspend fun deleteFavoriteFishById(id: Long)

    @Query("DELETE FROM FavoriteHabitat WHERE habitatId = :id")
    suspend fun deleteFavoriteHabitatById(id: Int)

    @Query("DELETE FROM favoritewatertype WHERE waterTypeId = :id")
    suspend fun deleteFavoriteWaterTypeById(id: Int)

    @Query("DELETE FROM FavoriteFishFamily WHERE fishFamilyId = :id")
    suspend fun deleteFavoriteFishFamilyById(id: Int)


    @Query("SELECT * FROM FavoriteFish WHERE userId = :userId")
    suspend fun getFavoriteFishByUser(userId: Int) : List<FavoriteFish>

    @Transaction
    suspend fun deleteFavoriteFishWithCascade(favFishId : Long, waterTypeId : Int, habitatId : Int, fishFamilyId : Int){
        deleteFavoriteFishById(favFishId)
        deleteFavoriteHabitatById(habitatId)
        deleteFavoriteFishFamilyById(fishFamilyId)
        deleteFavoriteWaterTypeById(waterTypeId)
    }
}