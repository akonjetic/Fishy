package hr.konjetic.fishy.network.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface FishyAPIService {

    @GET("api/fishfolio/users")
    suspend fun getAllUsers() : ArrayList<User>

    @POST("api/fishfolio/users")
    suspend fun postUser(@Body userDTO: UserPost): Response<Unit>

    @GET("api/fishfolio/fish")
    suspend fun getAllFishPaging(@Query("page") page: Int, @Query("pageSize") pageSize : Int) : FishResponse

    @GET("api/fishfolio/fish/names/{name}")
    suspend fun getFishByName(@Path("name") name : String) : ArrayList<Fish>

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

    @GET("api/fishfolio/fish/fishfamily/incompatibility")
    suspend fun getAllIncompatibleData() : ArrayList<FishFamilyCompatibility>

    @DELETE("api/fishfolio/fish/{id}")
    suspend fun deleteFishById(@Path("id") id: Int) : Response<Unit>

}