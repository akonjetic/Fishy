package hr.konjetic.fishy.network.model

import java.io.Serializable

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
) : Serializable

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

