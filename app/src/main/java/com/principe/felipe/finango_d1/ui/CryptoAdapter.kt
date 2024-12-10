package com.principe.felipe.finango_d1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.principe.felipe.finango_d1.databinding.ItemCryptoBinding
import com.principe.felipe.finango_d1.model.Crypto

class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    private val cryptos = mutableListOf<Crypto>()

    fun submitList(newCryptos: List<Crypto>) {
        cryptos.clear()
        cryptos.addAll(newCryptos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(cryptos[position])
    }

    override fun getItemCount(): Int = cryptos.size

    class CryptoViewHolder(private val binding: ItemCryptoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crypto: Crypto) {
            binding.tvCryptoSymbol.text = crypto.symbol
            binding.tvCryptoName.text = crypto.name
            binding.tvCryptoPrice.text = "Price: ${crypto.current_price}$"
            Glide.with(binding.ivCryptoImage.context).load(crypto.image).into(binding.ivCryptoImage)
        }
    }
}
