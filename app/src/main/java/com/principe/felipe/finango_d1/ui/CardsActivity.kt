package com.principe.felipe.finango_d1.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.principe.felipe.finango_d1.R


class CardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        val tvBankName = findViewById<TextView>(R.id.tvBankName)
        val tvCard1 = findViewById<TextView>(R.id.tvCard1)
        val btnCard1 = findViewById<Button>(R.id.btnCard1)
        val tvCard2 = findViewById<TextView>(R.id.tvCard2)
        val btnCard2 = findViewById<Button>(R.id.btnCard2)
        val tvCard3 = findViewById<TextView>(R.id.tvCard3)
        val btnCard3 = findViewById<Button>(R.id.btnCard3)

        val bankName = intent.getStringExtra("bankName")
        tvBankName.text = bankName

        when (bankName) {
            "BCP" -> {
                tvCard1.text = "Tarjeta Visa Clásica BCP\nUna tarjeta básica para compras nacionales e internacionales."
                tvCard2.text = "Tarjeta Visa Gold BCP\nOfrece beneficios adicionales como seguros de viaje."
                tvCard3.text = "Tarjeta Visa Signature BCP\nIncluye servicios exclusivos como acceso a salas VIP."
            }
            "Scotiabank" -> {
                tvCard1.text = "Tarjeta Scotia Visa Clásica\nIdeal para el día a día."
                tvCard2.text = "Tarjeta Scotia Visa Gold\nDescuentos en comercios y seguros de compra."
                tvCard3.text = "Tarjeta Scotia Travel Visa\nAcumula millas y descuentos en vuelos."
            }
        }

        val address = if (bankName == "BCP") {
            "Av. Carretera Central 111 Nivel 1 Int. LF-3A Lima Map, Santa Anita 15008"
        } else {
            "Av. Francisco Bolognesi 290, Santa Anita 15008"
        }

        btnCard1.setOnClickListener { goToMap(address) }
        btnCard2.setOnClickListener { goToMap(address) }
        btnCard3.setOnClickListener { goToMap(address) }
    }

    private fun goToMap(address: String) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("address", address)
        startActivity(intent)
    }
}
