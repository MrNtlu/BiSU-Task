package com.mrntlu.bisu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.bisu.R
import com.mrntlu.bisu.databinding.CellNewsItemBinding
import com.mrntlu.bisu.interfaces.Interaction
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.util.loadWithGlide
import com.mrntlu.bisu.util.setGone
import com.mrntlu.bisu.util.setVisible

class NewsAdapter(
    private val interaction: Interaction<Article>,
): PagingDataAdapter<Article, NewsAdapter.NewsItemViewHolder>(Comparator) {

    private val favSet: MutableSet<String> = mutableSetOf()
    private var isFavListInit = false

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
                val isFavAdded = favSet.contains(article.url)

                titleText.text = article.title
                descriptionText.text = article.description

                if (article.urlToImage != null) {
                    thumbnailImage.setVisible()
                    thumbnailImageProgress.setVisible()
                    thumbnailImage.loadWithGlide(article.urlToImage, thumbnailImageProgress)
                } else {
                    thumbnailImage.setGone()
                    thumbnailImageProgress.setGone()
                }

                root.setOnClickListener {
                    interaction.onItemSelected(position, article)
                }

                favouriteButton.setOnClickListener {
                    interaction.onFavTogglePressed(position, article, isFavAdded)
                }

                favouriteButton.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        if (isFavAdded) R.color.yellow500 else R.color.purple_500
                    )
                )
            }
        }
    }

    fun submitFavouriteListUpdated(favouriteList: List<String>) {
        if (!isFavListInit) {
            favSet.addAll(favouriteList)

            snapshot().items.forEachIndexed { index, article ->
                if (favSet.contains(article.url)) {
                    notifyItemChanged(index)
                }
            }

            isFavListInit = true
        } else {
            // Cloning the list to prevent ConcurrentModificationException
            val tempFavSet = favSet.toMutableList()
            val tempFavList = favouriteList.toMutableList()

            tempFavSet.forEachIndexed { index, article -> // Check removed favs
                if (!tempFavList.contains(article)) {
                    favSet.remove(favSet.elementAt(index))
                    notifyItemChanged(snapshot().items.map { it.url }.indexOf(article))
                    return
                }
            }

            tempFavList.forEachIndexed { index, article -> // Check added favs
                if (!tempFavSet.contains(article)) {
                    favSet.add(favouriteList[index])
                    notifyItemChanged(snapshot().items.map { it.url }.indexOf(article))
                    return
                }
            }
        }
    }

    class NewsItemViewHolder(val binding: CellNewsItemBinding): RecyclerView.ViewHolder(binding.root)
}