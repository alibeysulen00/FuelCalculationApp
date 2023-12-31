package app.Fuel.Calculate

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FuelCalculate {


    @RequiresApi(Build.VERSION_CODES.O)
    fun localDate():Pair<String,String>{
        val oneWeekAgo = LocalDate.now().minusWeeks(1)
        //tarihi istenen formata dönüştür ve döndür
        val startDate = formatDate(oneWeekAgo)
        //Bugünün tarihini al
        val today = LocalDate.now()
        val endDate = formatDate(today)
        return Pair(startDate,endDate)
    }

    fun formatDate(date:LocalDate):String{

        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return date.format(formatter)
    }


}