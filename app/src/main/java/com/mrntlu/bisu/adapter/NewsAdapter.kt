package com.mrntlu.bisu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.bisu.databinding.CellNewsItemBinding
import com.mrntlu.bisu.models.response.Article

class NewsAdapter(
): PagingDataAdapter<Article, NewsAdapter.NewsItemViewHolder>(Comparator) {
    object Comparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        return NewsItemViewHolder(
            CellNewsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        getItem(position)?.let { article ->
            holder.binding.apply {
                titleText.text = article.title
            }
        }
    }

    inner class NewsItemViewHolder(val binding: CellNewsItemBinding): RecyclerView.ViewHolder(binding.root)
}