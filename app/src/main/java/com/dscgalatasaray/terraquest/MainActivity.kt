package com.dscgalatasaray.terraquest

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.dscgalatasaray.terraquest.ui.theme.TerraQuestTheme
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : ComponentActivity() {

    private lateinit var sharedPref: SharedPreferences
    private val QUESTION_PREFIX = "question_"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = this.getSharedPreferences("com.dscgalatasaray.terraquest", MODE_PRIVATE)
        soruKaydet()
        setContent {
            TerraQuestTheme {
                Surface(modifier = Modifier.fillMaxSize()
                ){
                    SoruMakine(sorular = soruAl(), modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                    )
                }
            }
        }
    }

    private fun soruKaydet() {
        // Clear existing questions
        sharedPref.edit().remove("totalQuestions").apply()

        val testQuestions = mutableListOf(
            Soru<Boolean>(soruMetni = "Altının simgesi \"Au\" mudur?", cevap = true, zorluk = Zorluk.ORTA),
            Soru<Int>(soruMetni = "Su kaç derecede kaynar?", cevap = 100, zorluk = Zorluk.ZOR),
            Soru<String>(soruMetni = "Eski kelimesinin zıt anlamlısı nedir?", cevap = "yeni", zorluk = Zorluk.ORTA),
            Soru<Boolean>(soruMetni = "Penguenler uçar mı?", cevap = false, zorluk = Zorluk.KOLAY),
            Soru<Int>(soruMetni = "İstanbul'da kaç belediye vardır", cevap = 40, zorluk = Zorluk.ORTA)
        )

        // Save each question in SharedPreferences
        testQuestions.forEachIndexed { index, question ->
            val questionKey = QUESTION_PREFIX + index
            val questionString = Gson().toJson(question)

            sharedPref.edit().apply {
                putString(questionKey, questionString)
                apply()
            }
        }

        // Save the total number of questions
        sharedPref.edit().putInt("totalQuestions", testQuestions.size).apply()
    }

    private fun soruAl(): MutableList<Soru<*>> {
        val totalQuestions = sharedPref.getInt("totalQuestions", 0)

        // Retrieve each question from SharedPreferences
        return (0 until totalQuestions).mapNotNull { index ->
            val questionKey = QUESTION_PREFIX + index
            val questionString = sharedPref.getString(questionKey, null)

            if (questionString != null) {
                Gson().fromJson<Soru<*>>(questionString, object : TypeToken<Soru<*>>() {}.type)
            } else {
                null
            }
        }.toMutableList()
    }
}

data class Soru<T>(val soruMetni: String, val cevap: T, val zorluk: Zorluk){
}

enum class Zorluk{
    KOLAY, ORTA, ZOR
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SoruMakinePreview() {
    val sampleSorular = listOf(
        Soru<Boolean>("Altının simgesi \"Au\" mudur?", true, Zorluk.ORTA),
        Soru<Int>("Su kaç derecede kaynar?", 100, Zorluk.ZOR),
        Soru<String>("Eski kelimesinin zıt anlamlısı nedir?", "yeni", Zorluk.ORTA),
        Soru<Boolean>("Penguenler uçar mı?", false, Zorluk.KOLAY),
        Soru<Int>("İstanbul'da kaç belediye vardır", 40, Zorluk.ORTA)
    )

    SoruMakine(sorular = sampleSorular)
}

@Composable
fun SoruMakine(sorular: List<Soru<*>>, modifier: Modifier = Modifier){




    /*val soru1 = Soru<Boolean>(soruMetni = "Altının simgesi \"Au\" mudur?", cevap = true, zorluk = Zorluk.ORTA)
    val soru2 = Soru<Int>(soruMetni = "Su kaç derecede kaynar?", cevap = 100, zorluk = Zorluk.ZOR)
    val soru3 = Soru<String>(soruMetni = "Eski kelimesinin zıt anlamlısı nedir?", cevap = "yeni", zorluk = Zorluk.ORTA)
    val soru4 = Soru<Boolean>(soruMetni = "Penguenler uçar mı?", cevap = false, zorluk = Zorluk.KOLAY)
    val soru5 = Soru<Int>(soruMetni = "İstanbul'da kaç belediye vardır", cevap = 40, zorluk = Zorluk.ORTA)*/

    //var soruListesi = mutableListOf(soru1, soru2, soru3, soru4, soru5)
    var soruListesi = sorular

    var i by remember{ mutableStateOf(0) }
    var soruListesiMaxIndex = soruListesi.size-1
    var oncekiSoruAktif: Boolean = i>0
    var sonrakiSoruAktif : Boolean = i<soruListesiMaxIndex
    //Max index'e gore karsilastirip enable olup olmamasına karar verdim

    val mevcutSoru = soruListesi[i]


    val soruZorlugu = when(mevcutSoru.zorluk) {
        Zorluk.KOLAY -> "Kolay"
        Zorluk.ORTA -> "Orta"
        Zorluk.ZOR -> "Zor"
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(onClick = { /*TODO*/ }) {
                
            }
            Button(onClick = { /*TODO*/ }) {
                
            }
        }


        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = mevcutSoru.soruMetni,
                fontSize = 5.em
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Zorluk: $soruZorlugu",
                fontSize = 3.em
            )
            Spacer(modifier = Modifier.height(10.dp))
            when(mevcutSoru.cevap) {
                is Boolean -> BooleanCevapAlani(dogruCevap = mevcutSoru.cevap)
                is Number -> IntCevapAlani(dogruCevap = mevcutSoru.cevap.toInt())
                is String -> StringCevapAlani(dogruCevap = mevcutSoru.cevap)
                else -> println("bilinmeyen soru tipi")
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
        
        Row {
            Button(
                onClick = {i--},
                enabled = oncekiSoruAktif
            ) {
                Text(text = "Önceki Soru")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {i++},
                enabled = sonrakiSoruAktif
                ) {
                Text(text = "Sonraki Soru")
            }
        }

    }
}



@Composable
fun StringCevapAlani(dogruCevap : String){
    val context = LocalContext.current
    var cevapAlani by remember { mutableStateOf("") }
    var cevapAlaniRengi by remember { mutableStateOf(Color.Transparent) }

    Row {
        TextField(
            modifier = Modifier.background(cevapAlaniRengi),
            label = { Text(text = "Cevap alanı")},
            value = cevapAlani,
            onValueChange = {cevapAlani = it},
        )
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {
            if (cevapAlani == dogruCevap){
                cevapAlaniRengi = Color.Green
                Toast.makeText(context, "Cevap doğru!", Toast.LENGTH_SHORT).show()
            } else{
                cevapAlaniRengi = Color.Red
                Toast.makeText(context, "Cevap yanlış!",Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Kontrol")
        }
    }
}

@Composable
fun IntCevapAlani(dogruCevap : Int){
    val context = LocalContext.current
    var cevapAlani by remember { mutableStateOf("") }
    var cevapAlaniRengi by remember { mutableStateOf(Color.Transparent) }

    Row {
        TextField(
            modifier = Modifier.background(cevapAlaniRengi),
            label = { Text(text = "Cevap alanı")},
            value = cevapAlani,
            onValueChange = {cevapAlani = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {
            if (cevapAlani.toIntOrNull() == dogruCevap){
                cevapAlaniRengi = Color.Green
                Toast.makeText(context, "Cevap doğru!", Toast.LENGTH_SHORT).show()
            } else{
                cevapAlaniRengi = Color.Red
                Toast.makeText(context, "Cevap yanlış!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Kontrol")
        }
    }
}


@Composable
fun BooleanCevapAlani(dogruCevap: Boolean){
    val context = LocalContext.current
    var evetButonRengi by remember { mutableStateOf(Color.Blue) }
    var hayirButonRengi by remember { mutableStateOf(Color.Blue) }


    Row {
        Button(
            colors = ButtonDefaults.buttonColors(evetButonRengi),
            onClick = {

                if (dogruCevap){
                    evetButonRengi = Color.Green
                    hayirButonRengi = Color.Red
                    Toast.makeText(context, "Cevap doğru!", Toast.LENGTH_SHORT).show()

                } else {
                    evetButonRengi = Color.Red
                    hayirButonRengi = Color.Green
                    Toast.makeText(context, "Cevap yanlış!", Toast.LENGTH_SHORT).show()
                }
            }) {
            Text(text = "Evet")
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            colors = ButtonDefaults.buttonColors(hayirButonRengi),
            onClick = {
                if (!dogruCevap) {
                    evetButonRengi = Color.Red
                    hayirButonRengi = Color.Green
                    Toast.makeText(context, "Cevap doğru!", Toast.LENGTH_SHORT).show()

                } else {
                    evetButonRengi = Color.Green
                    hayirButonRengi = Color.Red
                    Toast.makeText(context, "Cevap yanlış!", Toast.LENGTH_SHORT).show()
                }
            }) {
            Text(text = "Hayır")
            //yusuf
            //deneme commiti
            //azat
            //yeni degisiklik
            //erman
        }

    }
}