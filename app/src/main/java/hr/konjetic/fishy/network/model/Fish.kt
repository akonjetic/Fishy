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
val MinAquariumSizeInL: Int,
val gender: String,
val maxNumberOfSameGender: Int,
val availableInStore: Int
) : Serializable {

    fun toDatabaseEntity() : AquariumFish{
        return AquariumFish(fishId = id, name = name, description = description, waterType = FavoriteWaterType(waterType.id, waterType.type), fishFamily = FavoriteFishFamily(fishFamily.id, fishFamily.name), habitat = FavoriteHabitat(habitat.id, habitat.name),
        image, minSchoolSize, avgSchoolSize, MinAquariumSizeInL, gender, maxNumberOfSameGender, availableInStore)
    }

    fun toDBEWithQuantity(quantity: Int) : AquariumFish{
        return AquariumFish(fishId = id, name = name, description = description, waterType = FavoriteWaterType(waterType.id, waterType.type), fishFamily = FavoriteFishFamily(fishFamily.id, fishFamily.name), habitat = FavoriteHabitat(habitat.id, habitat.name),
            image, minSchoolSize, avgSchoolSize, MinAquariumSizeInL, gender, maxNumberOfSameGender, quantity = quantity)
    }

    fun toDTO() : FishDTO{
        return FishDTO(name = name, description= description, waterTypeId = waterType.id, waterType =  waterType, fishFamilyId = fishFamily.id, fishFamily =  fishFamily,
            habitatId = habitat.id, habitat =  habitat, image =  image, minSchoolSize =  minSchoolSize, avgSchoolSize =  avgSchoolSize, minAquariumSizeInL =  MinAquariumSizeInL, gender =  gender,
            maxNumberOfSameGender = maxNumberOfSameGender, availableInStore =  availableInStore)
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
val minAquariumSizeInL: Int,
val gender: String,
val maxNumberOfSameGender: Int,
val availableInStore: Int
) : Serializable

data class FishFamilyCompatibility(
    val parentId : Int,
    val compatibilityId : Int
): Serializable
