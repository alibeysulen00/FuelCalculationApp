package app.Fuel.infoPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import app.Api.fuelcalculationapp.R

class InfoActivity : AppCompatActivity() {
    private lateinit var information: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        information = findViewById(R.id.information)



        val data = "<br><br><h1> Yakıt tüketimi nedir ?<br><br></h1> <p> Motorlu taşıtlar, hareket edebilmek için belirli bir enerjiye ihtiyaç duymaktadır. Bu enerji, genellikle araca depolanan yanıcı maddelerden elde edilmektedir. Araç sahipleri için belirli mesafeleri" +
                " katetmenin ne kadar yakıt harcanmasına sebep olduğu ve bunun maliyetini bilmek oldukça büyük önem taşımaktadır. <br><br>  <h1> Yakıt tüketimini hangi faktörler etkiler?</h1><br>" +
                "<p> Lastik basıncı, motor yağının durumu, kullanılan yanıcı madde türü (benzin, mazot vs.), klima gibi ek donanım kullanımları, aracın ağırlığı ve taşınan ek yükler gibi faktörler hareket edebilmek için harcanan enerji miktarını etkilemektedir. Bu faktörlere dikkat ederek ve sürüş esnasında motorun devrini ideale yakın tutarak yakıt tüketimi azaltılabilmektedir.<br><br>" +
                "<h1> Taşınan yük miktarı yakıt tüketimini nasıl etkiler?</h1><br>" +
                "<p>Aracın kendi ağırlığının yanında fazladan taşınan yükler de enerji harcamalarını artırmaktadır. Araçlarda fazladan taşınan her 45 kg ağırlığın, verimliliği yüzde 1-2 oranında düşürdüğü hesaplanmaktadır.<br><br>"+"" +
                "<h1> Yüksek hızda gitmek yakıt tüketimini düşürür mü?<br></h1>" +"<p>Araç ne kadar hızlı giderse o kadar fazla rüzgar direnci ile karşılaşmaktadır. Bu nedenle araç, hızını korumak için daha fazla yakıt harcamaya zorlanır. Saatte 12 km'lik bir farkın yakıt tüketimini yaklaşık olarak yüzde 23 oranında arttırabildiği hesaplanmaktadır. Ana yollarda hız sabitleyici yol bilgisayarını kullanmak verimliliği artıracağından, yakıt tüketimini azaltmaya yardımcı olmaktadır.<br><br>" +
                    "<h1> Arabam kaç yakar?</h1><br>" +"Aracınızın 100 km yol gidebilmek için kaç litre yakıt harcadığını ya da km başına kaç TL yaktığını hesaplama aracımız yardımıyla kolayca hesaplayabilirsiniz. Günümüzde ekonomi sınıfı son model binek araçlardan dizel motora sahip olanlar ortalama olarak 100 km'de 3 - 4 litre civarında mazot yakmaktadır. Benzinle çalışan motorlara sahip olan araçlar ise yaklaşık olarak 100 km'de 5 - 6 litre benzin yakmaktadır."



        information.text = Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)





    }
}