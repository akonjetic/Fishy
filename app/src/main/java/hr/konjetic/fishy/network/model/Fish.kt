package hr.konjetic.fishy.network.model

import hr.konjetic.fishy.database.entities.*
import java.io.Serializable

data class FishResponse(
    val totalCount : Int,
    val pageSize : Int,
    val currentPage : Int,
    val items: ArrayList<Fish>
): Serializable

data class Fish (
val id: Int,
val name: String,
val description: String,
val waterType: WaterType,
val fishFamily: FishFamily,
val habitat: Habitat,
val image: String,
val minSchoolSize: Int,
val avgSchoolSize: Int,
val gender: String,
val maxNumberOfSameGender: Int
) : Serializable {

    fun toDBEWithQuantity(quantity: Int) : AquariumFish{
        return AquariumFish(fishId = id, name = name, description = description, waterType = FavoriteWaterType(waterType.id, waterType.type), fishFamily = FavoriteFishFamily(fishFamily.id, fishFamily.name), habitat = FavoriteHabitat(habitat.id, habitat.name),
            image, minSchoolSize, avgSchoolSize, gender, maxNumberOfSameGender, quantity = quantity)
    }

}

data class FishFamily(
val id: Int,
val name: String
)  : Serializable

data class Habitat(
val id: Int,
val name: String
)  : Serializable

data class WaterType(
val id: Int,
val type: String
)  : Serializable

data class FishDTO (
val name: String,
val description: String,
val waterTypeId: Int,
val waterType: WaterType,
val fishFamilyId: Int,
val fishFamily: FishFamily,
val habitatId: Int,
val habitat: Habitat,
val image: String,
val minSchoolSize: Int,
val avgSchoolSize: Int,
val gender: String,
val maxNumberOfSameGender: Int
) : Serializable

data class FishFamilyCompatibility(
    val parentId : Int,
    val compatibilityId : Int
): Serializable
