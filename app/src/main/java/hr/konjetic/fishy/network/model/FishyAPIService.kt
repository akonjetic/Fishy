package hr.konjetic.fishy.network.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FishyAPIService {

    @GET("api/fishfolio/users")
    suspend fun getAllUsers() : ArrayList<User>

    @GET("api/fishfolio/{username}")
    suspend fun getByUsername(@Path("username") username : String) : Response<User>

    @POST("api/fishfolio")
    suspend fun postUser(@Body userDTO: UserPost): Response<Unit>
}