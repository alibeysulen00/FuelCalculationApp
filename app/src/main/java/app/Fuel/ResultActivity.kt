package app.Fuel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import app.Api.fuelcalculationapp.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var endTotalKmLiterText: TextView
    private lateinit var totalLitterText: TextView
    private lateinit var totalPriceText: TextView
    private lateinit var priceDateText: TextView
    private lateinit var lastDayPriceText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)




        endTotalKmLiterText = findViewById(R.id.EndTotalKmLiterText)
        totalLitterText = findViewById(R.id.totalLiterText)
        totalPriceText = findViewById(R.id.totalPriceText)
        priceDateText = findViewById(R.id.priceDateText)
        lastDayPriceText = findViewById(R.id.lastDayPriceText)

        val intent = intent
        val result = intent.getStringArrayListExtra("KEY")

        if (result != null && result.size >= 6) {
            val decimalFormat = DecimalFormat("#.#")
            val newKmLitter = decimalFormat.format(result[0].toDouble())
            val newTotalLitter = decimalFormat.format(result[1].toDouble())
            val newTotalPrice = decimalFormat.format(result[2].toDouble())
            val newPriceDate = formatDate(result[3])
            val newLastDayPrice = decimalFormat.format(result[4].toDouble())
            val newOneKmPrice = decimalFormat.format(result[5].toDouble())

            endTotalKmLiterText.setText("$newKmLitter L")
            totalLitterText.setText("$newTotalLitter L")
            totalPriceText.setText("$newTotalPrice TL")
            priceDateText.setText(newPriceDate)
            lastDayPriceText.setText("$newLastDayPrice TL/LT")
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