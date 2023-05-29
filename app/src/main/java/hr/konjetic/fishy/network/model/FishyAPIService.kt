package hr.konjetic.fishy.network.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FishyAPIService {

    @GET("api/fishfolio/users")
    suspend fun getAllUsers() : ArrayList<User>

    @GET("api/fishfolio/users/{username}")
    suspend fun getByUsername(@Path("username") username : String) : Response<User>

    @POST("api/fishfolio/users")
    suspend fun postUser(@Body userDTO: UserPost): Response<Unit>

    @GET("api/fishfolio/fish")
    suspend fun getAllFish() : ArrayList<Fish>

    @GET("api/fishfolio/fish/names/{name}")
    suspend fun getFishByName(@Path("name") name : String) : Response<Fish>

    @GET("api/fishfolio/fish/{id}")
    suspend fun getFishById(@Path("id") id : Int) : Response<Fish>

    @POST("api/fishfolio/fish")
    suspend fun postFish(@Body fishDTO: FishDTO): Response<ResponseBody>

    @GET("api/fishfolio/fish/habitat")
    suspend fun getAllHabitats() : ArrayList<Habitat>

    @GET("api/fishfolio/fish/fishfamilies")
    suspend fun getAllFishFamilies() : ArrayList<FishFamily>

    @GET("api/fishfolio/fish/watertype")
    suspend fun getAllWaterTypes() : ArrayList<WaterType>

}