package app.Fuel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import app.Api.fuelcalculationapp.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var oneKmLitter: EditText
    private lateinit var totalLitterText: TextView
    private lateinit var totalPriceText: TextView
    private lateinit var priceDateText: TextView
    private lateinit var motorinPrice: TextView
    private lateinit var benzinFiyat: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)




        oneKmLitter = findViewById(R.id.oneKmLiterText)
        totalLitterText = findViewById(R.id.totalLiterText)
        totalPriceText = findViewById(R.id.totalPriceText)
        priceDateText = findViewById(R.id.priceDateText)
        motorinPrice = findViewById(R.id.motorinFiyat)
        benzinFiyat = findViewById(R.id.benzinFiyat)

        val intent = intent
        val result = intent.getStringArrayListExtra("KEY")

        if (result != null && result.size >= 4) {
            val decimalFormat = DecimalFormat("#.#")
            val newKmLitter = decimalFormat.format(result[0].toDouble())
            val newTotalLitter = decimalFormat.format(result[1].toDouble())
            val newTotalPrice = decimalFormat.format(result[2].toDouble())
            val newPriceDate = formatDate(result[3])
            val motorinPrice1 = decimalFormat.format(result[4].toDouble())
            val benzinPrice1 = decimalFormat.format(result[5].toDouble())

            oneKmLitter.setText("$newKmLitter L")
            totalLitterText.setText("$newTotalLitter L")
            totalPriceText.setText("$newTotalPrice TL")
            priceDateText.setText(newPriceDate)
            motorinPrice.setText("$motorinPrice1 TL")
            benzinFiyat.setText("$benzinPrice1 TL")

        }
    }

    private fun formatDate(dateString: String): String {
        // Verilen tarihi ISO 8601 formatından çözümle
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date = parser.parse(dateString)

        // Tarihi gün, ay, yıl formatında biçimlendir
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}