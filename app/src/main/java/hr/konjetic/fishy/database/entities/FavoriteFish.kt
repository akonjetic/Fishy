package hr.konjetic.fishy.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import hr.konjetic.fishy.network.model.*
import java.io.Serializable

@Entity(tableName = "FavoriteFish")
data class FavoriteFish (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var userId: Int,
    val fishId: Int,
    val name: String,
    val description: String,
    @Embedded val waterType: FavoriteWaterType,
    @Embedded val fishFamily: FavoriteFishFamily,
    @Embedded val habitat: FavoriteHabitat,
    val image: String,
    val minSchoolSize: Int,
    val avgSchoolSize: Int,
    val gender: String,
    val maxNumberOfSameGender: Int
) : Serializable{

    fun toFishResponseData(): Fish{
        return Fish(
            fishId, name, description, waterType.mapToWaterTypeResponseData(), fishFamily.mapToFishFamilyResponseData(), habitat.mapToHabitatResponseData(), image, minSchoolSize, avgSchoolSize, gender, maxNumberOfSameGender
        )
    }
}