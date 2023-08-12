package app.Fuel

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import app.Api.fuelcalculationapp.R
import app.Fuel.Api.FuelPriceRepository
import app.Fuel.Api.FuelPriceResponse
import app.Fuel.Api.RetrofitClient
import app.Fuel.Calculate.FuelCalculate
import app.Fuel.infoPage.InfoActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

class MainActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"

    val fuelCalculateArrayList = ArrayList<String>()
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
    var lastDayPriceMotorin = 0.0

    private lateinit var distance: EditText
    private lateinit var km: EditText
    private lateinit var calculate: ImageButton
    private lateinit var fuelPriceRepository: FuelPriceRepository
    private lateinit var kg: EditText
    private lateinit var speed: EditText
    private lateinit var info: TextView
    private lateinit var radioGroup : RadioGroup
    private lateinit var benzinRadio: RadioButton
    private lateinit var motorinRadio: RadioButton
    private lateinit var banner1: AdView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-1301452834533419/7744488682", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })






        RetrofitClient.fuelPriceRepository.let {
            fuelPriceRepository = it
        }
        val calculator = FuelCalculate()

        distance = findViewById(R.id.distance_id)
        km = findViewById(R.id.km_id)
        calculate = findViewById(R.id.calculate_btn)
        calculate.setBackgroundColor(0x00000000)
        kg = findViewById(R.id.kg_id)
        speed = findViewById(R.id.speed_id)
        info = findViewById(R.id.info_id)
        radioGroup = findViewById(R.id.radioGroup)
        benzinRadio = findViewById(R.id.benzinRadio)
        motorinRadio = findViewById(R.id.motorinRadio)

        info.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        benzinRadio.setOnClickListener {
            // Benzin seçiliyken yapılacak işlemler
            lastDayPriceMotorin = 0.0 // Motorin fiyatını sıfırla
            lastDayPrice2 = getLastDayPrice("Kurşunsuz Benzin 95")
        }

        motorinRadio.setOnClickListener {
            // Motorin seçiliyken yapılacak işlemler
            lastDayPrice2 = 0.0 // Benzin fiyatını sıfırla
            lastDayPriceMotorin = getLastDayPrice("Motorin EcoForce")
        }

        calculate.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {

                        try {
                            val distanceText = distance.text.toString()
                            val kmText = km.text.toString()
                            val kgText = kg.text.toString()
                            val speedText = speed.text.toString()

                            if (distanceText.isNotEmpty() && kmText.isNotEmpty() && kgText.isNotEmpty() && speedText.isNotEmpty()) {
                                // ... Diğer işlemler
                                val (startDate, endDate) = calculator.localDate()
                                if (benzinRadio.isChecked) {
                                    getFuelPricesByDate("34", startDate, endDate, true) // Benzin selected
                                } else if (motorinRadio.isChecked) {
                                    getFuelPricesByDate("34", startDate, endDate, false) // Motorin selected
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            e.message?.let { it1 -> Toast.makeText(this@MainActivity, it1, Toast.LENGTH_SHORT).show() }
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d(TAG, "Ad failed to show.")
                        try {
                            val distanceText = distance.text.toString()
                            val kmText = km.text.toString()
                            val kgText = kg.text.toString()
                            val speedText = speed.text.toString()

                            if (distanceText.isNotEmpty() && kmText.isNotEmpty() && kgText.isNotEmpty() && speedText.isNotEmpty()) {
                                // ... Diğer işlemler
                                val (startDate, endDate) = calculator.localDate()
                                if (benzinRadio.isChecked) {
                                    getFuelPricesByDate("34", startDate, endDate, true) // Benzin selected
                                } else if (motorinRadio.isChecked) {
                                    getFuelPricesByDate("34", startDate, endDate, false) // Motorin selected
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            e.message?.let { it1 -> Toast.makeText(this@MainActivity, it1, Toast.LENGTH_SHORT).show() }
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        // Reklam tamamlanmadan kapatıldığında yapılacak işlemler burada olacak
                        mInterstitialAd = null
                    }
                }

                mInterstitialAd?.show(this)
            } else {
                // Reklam yüklenmediğinde yapılacak işlemler burada olacak
                try {
                    val distanceText = distance.text.toString()
                    val kmText = km.text.toString()
                    val kgText = kg.text.toString()
                    val speedText = speed.text.toString()

                    if (distanceText.isNotEmpty() && kmText.isNotEmpty() && kgText.isNotEmpty() && speedText.isNotEmpty()) {
                        // ... Diğer işlemler
                        val (startDate, endDate) = calculator.localDate()
                        if (benzinRadio.isChecked) {
                            getFuelPricesByDate("34", startDate, endDate, true) // Benzin selected
                        } else if (motorinRadio.isChecked) {
                            getFuelPricesByDate("34", startDate, endDate, false) // Motorin selected
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.message?.let { it1 -> Toast.makeText(this@MainActivity, it1, Toast.LENGTH_SHORT).show() }
                }
            }
        }



    }




    private fun getLastDayPrice(fuelType: String): Double {
        // En son fiyatı döndüren işlem
        // İlgili API çağrısı yapılabilir
        // Bu örnekte manuel olarak değer dönülüyor
        return 5.7
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
                            lastDayPriceMotorin = lastDayData.prices.lastOrNull { it.productName == "Motorin EcoForce" }?.amount!!
                            lastDayPrice2 = lastDayPrice!!
                            val priceDate = lastDayData.day
                            priceDate2 = priceDate
                            if (lastDayPrice != null) {
                                if (benzinRadio.isChecked) {
                                    // Benzin seçili ise
                                    oneKm = km.text.toString().toDouble() / 100
                                    oneKmPrice = oneKm * lastDayPrice
                                    total = oneKmPrice * distance.text.toString().toDouble()
                                    oneKgPrice = 2.0 / 45.0
                                    totalKg = kg.text.toString().toDouble() * oneKgPrice
                                    totalConsumption = 0.0
                                    kmDifference = speed.text.toString().toDouble() - 100
                                    speedPercent = (kmDifference * 23) / 12
                                    totalLitter = distance.text.toString().toDouble() * oneKm

                                    if (kg.text.isNotEmpty() && speed.text.toString().toInt() > 100) {
                                        totalConsumption = ((km.text.toString().toDouble() * totalKg) / 100) + km.text.toString().toDouble()
                                        totalConsumption = totalConsumption + (totalConsumption * speedPercent) / 100
                                        oneKm = totalConsumption / 100
                                        oneKmPr = oneKm * lastDayPrice
                                        oneKmPrice = oneKm * lastDayPrice
                                        total = oneKmPrice * distance.text.toString().toDouble()

                                        goToAddArrayList()
                                        goToIntent()
                                    } else {
                                        totalConsumption = km.text.toString().toDouble()
                                        oneKm = totalConsumption / 100
                                        oneKmPr = oneKm * lastDayPrice

                                        oneKmPrice = oneKm * lastDayPrice
                                        total = oneKmPrice * distance.text.toString().toDouble()

                                        goToAddArrayList()
                                        goToIntent()
                                    }
                                } else if (motorinRadio.isChecked) {
                                    // Motorin seçili ise
                                    oneKm = km.text.toString().toDouble() / 100
                                    oneKmPrice = oneKm * lastDayPriceMotorin // Motorin fiyatı üzerinden işlem yapılıyor
                                    total = oneKmPrice * distance.text.toString().toDouble()
                                    oneKgPrice = 2.0 / 45.0
                                    totalKg = kg.text.toString().toDouble() * oneKgPrice
                                    totalConsumption = 0.0
                                    kmDifference = speed.text.toString().toDouble() - 100
                                    speedPercent = (kmDifference * 23) / 12
                                    totalLitter = distance.text.toString().toDouble() * oneKm

                                    if (kg.text.isNotEmpty() && speed.text.toString().toInt() > 100) {
                                        totalConsumption = ((km.text.toString().toDouble() * totalKg) / 100) + km.text.toString().toDouble()
                                        totalConsumption = totalConsumption + (totalConsumption * speedPercent) / 100
                                        oneKm = totalConsumption / 100
                                        oneKmPr = oneKm * lastDayPriceMotorin // Motorin fiyatı üzerinden işlem yapılıyor
                                        oneKmPrice = oneKm * lastDayPriceMotorin // Motorin fiyatı üzerinden işlem yapılıyor
                                        total = oneKmPrice * distance.text.toString().toDouble()

                                        goToAddArrayList()
                                        goToIntent()
                                    } else {
                                        totalConsumption = km.text.toString().toDouble()
                                        oneKm = totalConsumption / 100
                                        oneKmPr = oneKm * lastDayPriceMotorin // Motorin fiyatı üzerinden işlem yapılıyor

                                        oneKmPrice = oneKm * lastDayPriceMotorin // Motorin fiyatı üzerinden işlem yapılıyor
                                        total = oneKmPrice * distance.text.toString().toDouble()

                                        goToAddArrayList()
                                        goToIntent()
                                    }
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "Bir hata oluştu: Benzin fiyatı alınamadı", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Toast.makeText(this@MainActivity, "Bir hata oluştu: Benzin fiyatları alınamadı", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
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
        fuelCalculateArrayList.add(lastDayPriceMotorin.toString())
        fuelCalculateArrayList.add(lastDayPrice2.toString())
    }
}
