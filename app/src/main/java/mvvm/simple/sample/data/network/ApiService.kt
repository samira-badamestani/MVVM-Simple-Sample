package mvvm.simple.sample.data.network


import mvvm.simple.sample.data.model.FoodDto
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("api/")
    fun getHome(
    ): Call<FoodDto>


}