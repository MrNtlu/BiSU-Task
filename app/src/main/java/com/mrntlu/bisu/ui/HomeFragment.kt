package com.mrntlu.bisu.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.bisu.adapter.NewsAdapter
import com.mrntlu.bisu.adapter.NewsLoadingAdapter
import com.mrntlu.bisu.databinding.FragmentHomeBinding
import com.mrntlu.bisu.interfaces.Interaction
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.util.Constants.TAG
import com.mrntlu.bisu.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    //TODO: Implement Country Selection

    private val viewModel: NewsViewModel by viewModels()
    private var newsAdapter: NewsAdapter = NewsAdapter()
    private var newsJob: Job? = null

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
        setObserver()
    }

    private fun setListener() {
        binding.btnRetry.setOnClickListener { newsAdapter?.retry() }
    }

    private fun setRecyclerView() {
        binding.newsRV.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            newsAdapter = NewsAdapter()
            adapter = newsAdapter.withLoadStateFooter(
                footer = NewsLoadingAdapter()
            )
        }

        newsAdapter.addLoadStateListener { loadState ->

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

    private fun setObserver() {
        newsJob?.cancel()

        newsJob = lifecycleScope.launch {
            viewModel.getBreakingNews().observe(viewLifecycleOwner) {
                newsAdapter.submitData(this@HomeFragment.lifecycle, it)
            }
        }
    }

    override fun onDestroy() {
        newsJob?.cancel()
        super.onDestroy()
    }
}