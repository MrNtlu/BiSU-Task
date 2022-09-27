package com.mrntlu.bisu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.mrntlu.bisu.R
import com.mrntlu.bisu.adapter.CountrySelectionAdapter
import com.mrntlu.bisu.adapter.NewsAdapter
import com.mrntlu.bisu.adapter.NewsLoadingAdapter
import com.mrntlu.bisu.databinding.FragmentHomeBinding
import com.mrntlu.bisu.interfaces.Interaction
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.viewmodel.FavouritesViewModel
import com.mrntlu.bisu.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: NewsViewModel by viewModels()
    private val favsViewModel: FavouritesViewModel by viewModels()
    private var newsAdapter: NewsAdapter? = null
    private var countrySelectionAdapter: CountrySelectionAdapter? = null
    private var newsJob: Job? = null
    private val firebaseAnalytics = Firebase.analytics
    private var newsCountry = "us"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setRecyclerView()
        setCountryChipRecyclerView()
        setObserver()
    }

    private fun setListener() {
        binding.btnRetry.setOnClickListener { newsAdapter?.retry() }
    }

    private fun setRecyclerView() {
        binding.newsRV.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            newsAdapter = NewsAdapter(object: Interaction<Article> {
                override fun onItemSelected(position: Int, item: Article) {
                    val bundle = item.toBundle()
                    navController.navigate(R.id.action_navigation_home_to_newsDetailScreen, bundle)
                }

                override fun onFavTogglePressed(position: Int, item: Article, isFavAdded: Boolean) {
                    if (isFavAdded) {
                        favsViewModel.deleteFavouriteNews(item.url)
                    } else {
                        favsViewModel.addNewFavouriteNews(item)
                        sendAnalyticsLog(item.url, item.title)
                    }
                }

            })
            adapter = newsAdapter?.withLoadStateFooter(
                footer = NewsLoadingAdapter()
            )
        }

        newsAdapter?.addLoadStateListener { loadState ->
            if (!isViewDestroyed) {
                binding.newsRV.isVisible = loadState.source.refresh is LoadState.NotLoading
                binding.loadingView.root.isVisible = loadState.source.refresh is LoadState.Loading
                binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                binding.errorTV.isVisible = loadState.source.refresh is LoadState.Error

                val error = when {
                    loadState.source.prepend is LoadState.Error -> loadState.source.prepend as LoadState.Error
                    loadState.source.append is LoadState.Error -> loadState.source.append as LoadState.Error
                    loadState.source.refresh is LoadState.Error -> loadState.source.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    binding.errorTV.text = it.error.localizedMessage
                }
            }
        }
    }

    private fun setCountryChipRecyclerView() {
        binding.countryChipRV.apply {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            countrySelectionAdapter = CountrySelectionAdapter(newsCountry) { code ->
                countrySelectionAdapter?.changeSelection(code)
                newsCountry = code
                favsViewModel.onDestroy()
                setObserver()
            }
            adapter = countrySelectionAdapter
        }
    }

    private fun setObserver() {
        newsJob?.cancel()

        newsJob = lifecycleScope.launch {
            viewModel.getBreakingNews(newsCountry).collectLatest {
                newsAdapter?.submitData(it)
            }
        }

        favsViewModel.getAllFavouritesAndSetListener().observe(viewLifecycleOwner) { favourites ->
            newsAdapter?.submitFavouriteListUpdated(favourites.map { it.url })
        }
    }

    private fun sendAnalyticsLog(url: String, title: String) {
        firebaseAnalytics.logEvent("fav_added") {
            param("url", url)
            param("title", title)
        }
    }

    // To prevent memory leak
    override fun onDestroyView() {
        newsAdapter = null
        countrySelectionAdapter = null
        newsJob?.cancel()
        newsJob = null
        favsViewModel.onDestroy()
        super.onDestroyView()
    }
}