package com.mrntlu.bisu.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrntlu.bisu.R
import com.mrntlu.bisu.adapter.viewholder.CountryChipViewHolder
import com.mrntlu.bisu.databinding.CellCountryChipBinding
import com.mrntlu.bisu.models.CountryChip

class CountrySelectionAdapter(
    private var selectedCountry: String,
    private val onSelected: (code: String) -> Unit
): RecyclerView.Adapter<CountryChipViewHolder>() {

    private var countryList = listOf(
        CountryChip("us"),
        CountryChip("tr"),
        CountryChip("gb"),
        CountryChip("au"),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryChipViewHolder {
        return CountryChipViewHolder(
            CellCountryChipBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CountryChipViewHolder, position: Int) {
        holder.binding.apply {
            val countryChip = countryList[position]
            chipText.text = countryChip.code.uppercase()
            Glide.with(root.context).load(countryChip.flag).into(chipImage)

            chipCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if (selectedCountry == countryChip.code) R.color.purple_500 else R.color.purple_200
                )
            )

            root.setOnClickListener {
                onSelected(countryChip.code)
            }
        }
    }

    override fun getItemCount() = countryList.size

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelection(selected: String) {
        selectedCountry = selected
        notifyDataSetChanged()
    }
}