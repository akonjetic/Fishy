package hr.konjetic.fishy.database.entities

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.konjetic.fishy.network.model.Fish
import hr.konjetic.fishy.network.model.FishFamily
import hr.konjetic.fishy.network.model.Habitat
import hr.konjetic.fishy.network.model.WaterType
import java.io.Serializable

@Entity(tableName = "Aquarium")
data class Aquarium (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "aquariumId")
    val id: Long,
    @ColumnInfo(name = "aquariumUserId")
    var userId: Int,
    @ColumnInfo(name = "aquariumName")
    val name: String,
    val rating: Double?,
    @TypeConverters(FishListConverter::class)
    val fish: ArrayList<AquariumFish>
) : Serializable

class FishListConverter {
    @TypeConverter
    fun fromJson(json: String): ArrayList<AquariumFish> {
        val type = object : TypeToken<ArrayList<AquariumFish>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: ArrayList<AquariumFish>): String {
        return Gson().toJson(list)
    }
}

@Entity(tableName = "AquariumFish")
data class AquariumFish (
    @PrimaryKey
    @ColumnInfo(name = "aquariumFishId")
    val fishId: Int,
    @ColumnInfo(name = "aquariumFishName")
    val name: String,
    @ColumnInfo(name = "aquariumFishDescription")
    val description: String,
    @Embedded val waterType: FavoriteWaterType,
    @Embedded val fishFamily: FavoriteFishFamily,
    @Embedded val habitat: FavoriteHabitat,
    @ColumnInfo(name = "aquariumFishImage")
    val image: String,
    @ColumnInfo(name = "aquariumFishMinSchoolSize")
    val minSchoolSize: Int,
    @ColumnInfo(name = "aquariumFishAvgSchoolSize")
    val avgSchoolSize: Int,
    @ColumnInfo(name = "aquariumFishMinAquariumSizeL")
    val MinAquariumSizeInL: Int,
    @ColumnInfo(name = "aquariumFishGender")
    val gender: String,
    @ColumnInfo(name = "aquariumFishMaxNumOfSameGender")
    val maxNumberOfSameGender: Int,
    @ColumnInfo(name = "aquariumFishStoreQuantity")
    var quantity: Int
) : Serializable {
    fun toFish() : Fish{
        return Fish(id = fishId, name = name, description, waterType = WaterType(waterType.id, waterType.type), fishFamily = FishFamily(fishFamily.id, fishFamily.name), habitat = Habitat(habitat.id, habitat.name),
        image, minSchoolSize, avgSchoolSize, MinAquariumSizeInL, gender, maxNumberOfSameGender, quantity)
    }
}