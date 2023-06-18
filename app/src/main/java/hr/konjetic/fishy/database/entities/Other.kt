package hr.konjetic.fishy.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hr.konjetic.fishy.network.model.FishFamily
import hr.konjetic.fishy.network.model.Habitat
import hr.konjetic.fishy.network.model.WaterType
import java.io.Serializable



@Entity(tableName = "FavoriteWaterType")
data class FavoriteWaterType(
    @PrimaryKey
    @ColumnInfo(name = "waterTypeId")
    val id: Int,
    val type: String
) : Serializable {
    fun mapToWaterTypeResponseData(): WaterType {
        return WaterType(id, type)
    }
}

@Entity(tableName = "FavoriteHabitat")
data class FavoriteHabitat(
    @PrimaryKey
    @ColumnInfo(name = "habitatId")
    val id: Int,
    @ColumnInfo(name = "habitatName")
    val name: String
) : Serializable {

    fun mapToHabitatResponseData(): Habitat {
        return Habitat(id, name)
    }
}


@Entity(tableName = "FavoriteFishFamily")
data class FavoriteFishFamily(
    @PrimaryKey
    @ColumnInfo(name = "fishFamilyId")
    val id: Int,
    @ColumnInfo(name = "fishFamilyName")
    val name: String
) : Serializable {
    fun mapToFishFamilyResponseData(): FishFamily {
        return FishFamily(id, name)
    }
}