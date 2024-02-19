package g54720.quickfood.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit
import kotlinx.serialization.json.Json

import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL=
    "https://api.spoonacular.com"

private val networkConstants:NetworkConstants= NetworkConstants()

private val json = Json { ignoreUnknownKeys = true }

private val retrofit=Retrofit.Builder()
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .baseUrl(BASE_URL)
    .build()

interface SpoonacularApiService {
    @GET("/recipes/random")
    suspend fun getRecipeRandom(
        @Query("apiKey")apikey:String= networkConstants.apiKey,
        @Query("number")number:Int=1,
        @Query("tags")tags:String
    ):RecipeList

}
object SpoonacularApi{
    val retrofitService:SpoonacularApiService by lazy {
        retrofit.create(SpoonacularApiService::class.java)
    }
}