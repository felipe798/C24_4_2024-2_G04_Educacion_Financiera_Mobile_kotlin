package com.principe.felipe.finango_d1.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.principe.felipe.finango_d1.databinding.ActivityBanksBinding
import com.principe.felipe.finango_d1.ui.CardsActivity

class BanksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBanksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBanksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBcp.setOnClickListener {
            val intent = Intent(this, CardsActivity::class.java)
            intent.putExtra("bankName", "BCP")
            startActivity(intent)
        }

        binding.imgScotiabank.setOnClickListener {
            val intent = Intent(this, CardsActivity::class.java)
            intent.putExtra("bankName", "Scotiabank")
            startActivity(intent)
        }
    }
}
