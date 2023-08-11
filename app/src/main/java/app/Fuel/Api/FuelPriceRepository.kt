package app.Fuel.Api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FuelPriceRepository {
    @GET("api/fuelprices/prices/archive")
    fun getFuelPrices(
        @Query("DistrictCode") districtCode: String,
        @Query("StartDate") startDate: String,
        @Query("EndDate") endDate: String,
        @Query("IncludeAllProducts") includeAllProducts: Boolean
    ): Call<List<FuelPriceResponse>>
}