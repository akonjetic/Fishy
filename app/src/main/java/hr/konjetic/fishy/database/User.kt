package hr.konjetic.fishy.database

data class User (
 val email: String,
 val password: String,
 val userType: UserType
)

enum class UserType{
    BASIC, ADMIN
}