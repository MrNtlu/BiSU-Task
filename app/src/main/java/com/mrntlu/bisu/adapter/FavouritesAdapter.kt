package com.mrntlu.bisu.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.bisu.R
import com.mrntlu.bisu.adapter.viewholder.ErrorItemViewHolder
import com.mrntlu.bisu.adapter.viewholder.LoadingViewHolder
import com.mrntlu.bisu.adapter.viewholder.NoItemViewHolder
import com.mrntlu.bisu.databinding.CellErrorBinding
import com.mrntlu.bisu.databinding.CellLoadingBinding
import com.mrntlu.bisu.databinding.CellNewsItemBinding
import com.mrntlu.bisu.databinding.CellNoItemBinding
import com.mrntlu.bisu.interfaces.Interaction
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.util.loadWithGlide
import com.mrntlu.bisu.util.setGone
import com.mrntlu.bisu.util.setVisible

class FavouritesAdapter(
    private val interaction: Interaction<Article>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //Conditions
    private var isAdapterSet = false
    private var isErrorOccurred = false

    //Holders
    private val LOADING_ITEM_HOLDER = 0
    val ITEM_HOLDER = 1
    private val ERROR_HOLDER = 2
    private val NO_ITEM_HOLDER = 3

    private var errorMessage = "Error!"
    private var arrayList: ArrayList<Article> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER -> NoItemViewHolder(CellNoItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false))
            LOADING_ITEM_HOLDER -> LoadingViewHolder(CellLoadingBinding.inflate(LayoutInflater.from(parent.context), parent ,false))
            ERROR_HOLDER -> ErrorItemViewHolder(CellErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> NewsAdapter.NewsItemViewHolder(
                CellNewsItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            ITEM_HOLDER -> {
                (holder as NewsAdapter.NewsItemViewHolder).binding.apply {
                    val article = arrayList[position]

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
                        interaction.onFavTogglePressed(position, article, true)
                    }

                    favouriteButton.setColorFilter(
                        ContextCompat.getColor(
                            root.context,
                            R.color.yellow500
                        )
                    )
                }
            }
            ERROR_HOLDER -> {
                (holder as ErrorItemViewHolder).binding.apply {
                    errorText.text = errorMessage
                }
            }
        }
    }

    override fun getItemCount() = if (isAdapterSet && !isErrorOccurred && arrayList.size!=0) arrayList.size
    else if (isAdapterSet && !isErrorOccurred) arrayList.size + 1
    else 1

    override fun getItemViewType(position: Int)=if (isAdapterSet){
        when{
            isErrorOccurred -> ERROR_HOLDER
            arrayList.size == 0 -> NO_ITEM_HOLDER
            else -> ITEM_HOLDER
        }
    } else LOADING_ITEM_HOLDER

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Article>) {
        arrayList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet = true
        isErrorOccurred = false
        notifyDataSetChanged()
    }

    fun submitRemoveItem(article: Article) {
        val index = arrayList.indexOf(article)
        arrayList.remove(article)

        notifyItemRemoved(index)
    }

    fun submitError(message:String){
        isAdapterSet = true
        isErrorOccurred = true
        errorMessage = message
        notifyItemChanged(0)
    }
}