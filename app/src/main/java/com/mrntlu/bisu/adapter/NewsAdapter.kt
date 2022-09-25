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

    private val favSet: MutableSet<Article> = mutableSetOf()
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
                val isFavAdded = favSet.map { it.url }.contains(article.url)

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

    fun submitFavouriteListUpdated(favouriteList: ArrayList<Article>) {
        if (!isFavListInit) {
            favSet.addAll(favouriteList)

            snapshot().items.forEachIndexed { index, article ->
                if (favSet.map { it.url }.contains(article.url)) {
                    notifyItemChanged(index)
                }
            }

            isFavListInit = true
        } else {
            val tempFavSet = favSet.map { it.url }
            val tempFavList = favouriteList.map { it.url }

            //To check removed favs
            tempFavSet.forEachIndexed { index, article ->
                if (!tempFavList.contains(article)) {
                    favSet.remove(favSet.elementAt(index))
                    notifyItemChanged(snapshot().items.map { it.url }.indexOf(article))
                }
            }

            //To check added favs
            tempFavList.forEachIndexed { index, article ->
                if (!tempFavSet.contains(article)) {
                    favSet.add(favouriteList[index])
                    notifyItemChanged(snapshot().items.map { it.url }.indexOf(article))
                }
            }
        }
    }

    inner class NewsItemViewHolder(val binding: CellNewsItemBinding): RecyclerView.ViewHolder(binding.root)
}