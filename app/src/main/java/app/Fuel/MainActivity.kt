package app.Fuel

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import app.Api.fuelcalculationapp.R
import app.Fuel.Api.FuelPriceRepository
import app.Fuel.Api.FuelPriceResponse
import app.Fuel.Calculate.FuelCalculate
import app.Fuel.infoPage.InfoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
// İkinci activitye veri yollamak için Array List kullanıldı ve bu array listin içine veriler atıldı.
    val fuelCalculateArrayList = ArrayList<String>()
// Verileri sınıfın tüm noktalarında kullanabilmek için class altında tanımladım
    var oneKm = 0.0
    var oneKmPrice = 0.0
    var total = 0.0
    var oneKgPrice = 0.0
    var totalKg= 0.0
    var totalConsumption = 0.0
    var kmDifference= 0.0
    var speedPercent = 0.0
    var totalLitter = 0.0
    var priceDate2 = ""
    var lastDayPrice2 = 0.0
    var oneKmPr = 0.0



// Layout içindeki tanımlamaların hangi context türüne eşit olduğunu belittim
    private lateinit var distance: EditText
    private lateinit var km: EditText
    private lateinit var calculate: ImageButton
    private lateinit var fuelPriceRepository: FuelPriceRepository
    private lateinit var kg: EditText
    private lateinit var speed: EditText
    private lateinit var info:TextView




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculator = FuelCalculate()
//--------------------------Res dosyasındaki layout içindeki tanımlamalar------------------------------------------------------
        distance = findViewById(R.id.distance_id)
        distance.setText("0")
        km = findViewById(R.id.km_id)
        km.setText("0")
        calculate = findViewById(R.id.calculate_btn)
        calculate.setBackgroundColor(Color.TRANSPARENT)
        kg = findViewById(R.id.kg_id)
        kg.setText("0")
        speed = findViewById(R.id.speed_id)
        speed.setText("0")
        info = findViewById(R.id.info_id)

//--------------------------Retrofit ile base url tanımlanıp build edilmesi------------------------------------------------------

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.opet.com.tr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fuelPriceRepository = retrofit.create(FuelPriceRepository::class.java)

//--------------------------------------------------------------------------------------------------

        info.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)

        }

//---------------------------Butona basılınca uygulanacak işlemler aşağıda belirtildi---------------------------------------------------------
        calculate.setOnClickListener {

            val distanceText = distance.text.toString()
            val kmText = km.text.toString()
            val kgText = kg.text.toString()
            val speedText = speed.text.toString()

            if (distanceText.isNotEmpty() && kmText.isNotEmpty() && kgText.isNotEmpty() && speedText.isNotEmpty()) {
                try {
                    val distanceValue = distanceText.toDouble()
                    val kmValue = kmText.toDouble()
                    val kgValue = kgText.toDouble()
                    val speedValue = speedText.toDouble()

                    val (startDate, endDate) = calculator.localDate()

                    getFuelPricesByDate("34", startDate, endDate, true) // İstanbul için 34 kodu kullanıldı

                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Lütfen tüm alanlara sayısal değerler girin", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }

    }



    private fun getFuelPricesByDate(districtCode: String, startDate: String, endDate: String, includeAllProducts: Boolean) {
        val call = fuelPriceRepository.getFuelPrices(districtCode, startDate, endDate, includeAllProducts)
        call.enqueue(object : Callback<List<FuelPriceResponse>> {
            override fun onResponse(call: Call<List<FuelPriceResponse>>, response: Response<List<FuelPriceResponse>>) {
                if (response.isSuccessful) {
                    val fuelPriceResponses = response.body()
                    fuelPriceResponses?.let { fuelPriceResponseList ->
                        if (fuelPriceResponseList.isNotEmpty()) {
                            val lastDayData = fuelPriceResponseList.last()
                            val lastDayPrice = lastDayData.prices.lastOrNull { it.productName == "Kurşunsuz Benzin 95" }?.amount
                            lastDayPrice2 = lastDayPrice!!
                             val priceDate = lastDayData.day
                            priceDate2 = priceDate
                            if (lastDayPrice != null) {
                            // eğer son günün benzin verileri çekilebilirse aşağıdaki hesaplamalar yapılsın


                                 oneKm = km.text.toString().toDouble() / 100
                                 oneKmPrice = oneKm * lastDayPrice
                                 total = oneKmPrice * distance.text.toString().toDouble()
                                 oneKgPrice = 2.0 / 45.0
                                 totalKg = kg.text.toString().toDouble() * oneKgPrice
                                 totalConsumption = 0.0
                                 kmDifference = speed.text.toString().toDouble() - 100
                                 speedPercent = (kmDifference*23)/12
                                 totalLitter = distance.text.toString().toDouble()* oneKm

                                if(kg.text.isNotEmpty() && speed.text.toString().toInt()>100){

                                    totalConsumption = ((km.text.toString().toDouble() * totalKg) /100 ) + km.text.toString().toDouble()
                                    totalConsumption = totalConsumption + (totalConsumption * speedPercent)/100
                                    oneKm = totalConsumption/100
                                    oneKmPr = oneKm *lastDayPrice
                                    oneKmPrice = oneKm *lastDayPrice
                                    total = oneKmPrice * distance.text.toString().toDouble()
                                    println("if içerisine girdi")

                                    goToAddArrayList()

                                    goToIntent()
                                }
                                else{

                                    totalConsumption = km.text.toString().toDouble()
                                    oneKm = totalConsumption/100
                                    oneKmPr = oneKm *lastDayPrice

                                    oneKmPrice = oneKm *lastDayPrice
                                    total = oneKmPrice * distance.text.toString().toDouble()
                                    goToAddArrayList()
                                    goToIntent()
                                }

                            } else {
                                // lastDayPrice null ise, hata mesajı gösterin
                                Toast.makeText(this@MainActivity, "Bir hata oluştu: Benzin fiyatı alınamadı", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // fuelPriceResponseList boş ise, hata mesajı gösterin
                            Toast.makeText(this@MainActivity, "Bir hata oluştu: Benzin fiyatları alınamadı", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // response.isSuccessful değilse, hata mesajı gösterin
                    Toast.makeText(this@MainActivity, "Bir hata oluştu: Servis cevap vermedi", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<FuelPriceResponse>>, t: Throwable) {
        t.printStackTrace()
            }
        })
    }
    fun goToIntent(){
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("KEY", fuelCalculateArrayList)
        startActivity(intent)
    }

    fun goToAddArrayList(){
        fuelCalculateArrayList.clear()
        fuelCalculateArrayList.add(totalConsumption.toString())
        fuelCalculateArrayList.add(totalLitter.toString())
        fuelCalculateArrayList.add(total.toString())
        fuelCalculateArrayList.add(priceDate2)
        fuelCalculateArrayList.add(lastDayPrice2.toString())
        fuelCalculateArrayList.add(oneKmPr.toString())

    }

}


