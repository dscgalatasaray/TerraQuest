package com.dscgalatasaray.terraquest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerraQuestTheme {
                Surface(modifier = Modifier.fillMaxSize()
                ){
                    SoruMakine(modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                    )
                }
            }
        }
    }
}

data class Soru<T>(val soruMetni: String, val cevap: T, val zorluk: Zorluk){
}

enum class Zorluk{
    KOLAY, ORTA, ZOR
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SoruMakine(modifier: Modifier = Modifier){




    val soru1 = Soru<Boolean>(soruMetni = "Altının simgesi \"Au\" mudur?", cevap = true, zorluk = Zorluk.ORTA)
    val soru2 = Soru<Int>(soruMetni = "Su kaç derecede kaynar?", cevap = 100, zorluk = Zorluk.ZOR)
    val soru3 = Soru<String>(soruMetni = "Eski kelimesinin zıt anlamlısı nedir?", cevap = "yeni", zorluk = Zorluk.ORTA)
    val soru4 = Soru<Boolean>(soruMetni = "Penguenler uçar mı?", cevap = false, zorluk = Zorluk.KOLAY)
    val soru5 = Soru<Int>(soruMetni = "İstanbul'da kaç belediye vardır", cevap = 40, zorluk = Zorluk.ORTA)

    var soruListesi = mutableListOf(soru1, soru2, soru3, soru4, soru5)

    var i by remember{ mutableStateOf(0) }
    var oncekiSoruAktif: Boolean = i>0

    val mevcutSoru = soruListesi[i]


    val soruZorlugu = when(mevcutSoru.zorluk) {
        Zorluk.KOLAY -> "Kolay"
        Zorluk.ORTA -> "Orta"
        Zorluk.ZOR -> "Zor"
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
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
            is Int -> IntCevapAlani(dogruCevap = mevcutSoru.cevap)
            is String -> StringCevapAlani(dogruCevap = mevcutSoru.cevap)
            else -> println("bilinmeyen soru tipi")
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Button(
                onClick = {i--},
                enabled = oncekiSoruAktif
            ) {
                Text(text = "Önceki Soru")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {i++}) {
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
                Toast.makeText(context, "Cevap doğru!", Toast.LENGTH_SHORT)
            } else{
                cevapAlaniRengi = Color.Red
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
            //deneme commiti
            //azat
        }

    }
}