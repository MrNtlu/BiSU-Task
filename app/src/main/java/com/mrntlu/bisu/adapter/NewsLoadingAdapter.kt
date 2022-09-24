package com.mrntlu.bisu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.bisu.databinding.CellPaginationLoadingBinding

class NewsLoadingAdapter: LoadStateAdapter<NewsLoadingAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.root.isVisible = loadState is LoadState.Loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            CellPaginationLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class LoadStateViewHolder(val binding: CellPaginationLoadingBinding): RecyclerView.ViewHolder(binding.root)
}