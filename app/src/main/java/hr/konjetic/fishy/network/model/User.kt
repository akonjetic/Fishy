package hr.konjetic.fishy.network.model

import java.io.Serializable

data class User (
 val email: String,
 val password: String,
 val userType: Int,
 val username: String,
 val firstName: String,
 val lastName: String,
 val id: Int
)

data class UserPost(
 val email : String,
 val username : String,
 val userType : Int,
 val password : String
) : Serializable