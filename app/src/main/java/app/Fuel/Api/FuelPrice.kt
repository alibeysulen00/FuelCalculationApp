package app.Fuel.Api

data class FuelPrice(
    val priceDate: String,
    val productName: String,
    val productShortName: String,
    val productCode: String,
    val amount: Double
)