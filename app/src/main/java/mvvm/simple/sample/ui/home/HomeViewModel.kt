package mvvm.simple.sample.ui.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import mvvm.simple.sample.core.App
import mvvm.simple.sample.data.db.AppDatabase
import mvvm.simple.sample.data.model.Food
import mvvm.simple.sample.data.model.FoodDto
import mvvm.simple.sample.data.network.ApiService
import mvvm.simple.sample.ui.util.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    val apiService: ApiService = App.api!!,
    val appDatabase: AppDatabase = AppDatabase.getInstance()
): ViewModel(){

    private val TAG: String = HomeViewModel::class.java.simpleName
    val homeData: MutableLiveData<FoodDto> by lazy { MutableLiveData<FoodDto>() }
    val error : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val showToast : SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    init {
     getFoods()
    }
     fun getFoods() {
        apiService.getHome().enqueue(object : Callback<FoodDto?> {
            override fun onFailure(call: Call<FoodDto?>?, t: Throwable?) {
                error.postValue("error happened")
            }

            override fun onResponse(call: Call<FoodDto?>?, response: Response<FoodDto?>?) {
                Log.d(TAG, "onResponse() called with: call = [$call], response = [$response]")
                Log.d(TAG,
                    "isSuccessful : " + response?.isSuccessful
                            + " message : " + response?.message()
                            + " code : " + response?.code()
                            + " raw : " + response?.raw()
                )

                    if (response != null) {
                        if (response.isSuccessful) {
                            homeData.postValue(response.body())
                            showToast.postValue("data received (Toast shows one times)")
                            if (response.body()!!.results.size > 0) {
                                saveInDB(response.body()!!.results)
                            }

                        } else {
                          error.postValue(response.errorBody()!!.string())
                        }
                    }
                }

        })

    }
    private fun saveInDB(results: MutableList<Food>) {
            for (i in results) {
                Log.d(TAG, "${i.title} inserted to databade")
                appDatabase.foodDao().insertFood(i)
            }

    }
}