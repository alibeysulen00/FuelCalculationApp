package app.Fuel.Api

import app.Fuel.Api.FuelPrice


data class FuelPriceResponse(
    val day: String,
    val prices: List<FuelPrice>
)