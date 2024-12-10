package com.principe.felipe.finango_d1.ui



import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.principe.felipe.finango_d1.ViewModel.CryptoViewModel
import com.principe.felipe.finango_d1.ViewModel.CryptoViewModelFactory
import com.principe.felipe.finango_d1.databinding.ActivityCryptoListBinding
import com.principe.felipe.finango_d1.repository.CryptoRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CryptoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCryptoListBinding
    private val viewModel: CryptoViewModel by viewModels {
        CryptoViewModelFactory(CryptoRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CryptoAdapter()
        binding.recyclerCryptos.layoutManager = LinearLayoutManager(this)
        binding.recyclerCryptos.adapter = adapter

        lifecycleScope.launch {
            viewModel.cryptos.collectLatest { cryptos ->
                adapter.submitList(cryptos)
            }
        }

        viewModel.fetchCryptocurrencies("CG-2qeiYxWhDRDXsrYAgTpNhpFk")
    }
}
