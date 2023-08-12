package app.Fuel.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val fuelPriceRepository: FuelPriceRepository

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.opet.com.tr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fuelPriceRepository = retrofit.create(FuelPriceRepository::class.java)
    }
}