package com.mrntlu.bisu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import com.mrntlu.bisu.databinding.FragmentNewsDetailScreenBinding
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.models.response.Source
import com.mrntlu.bisu.ui.composeable.NewsDetailView
import com.mrntlu.bisu.util.printLog

class NewsDetailScreen : BaseFragment<FragmentNewsDetailScreenBinding>() {

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            article = Article(
                it.getString("author"),
                it.getString("content"),
                it.getString("description"),
                it.getString("publishedAt", ""),
                Source(),
                it.getString("title", ""),
                it.getString("url", ""),
                it.getString("urlToImage"),
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailScreenBinding.inflate(inflater, container, false)
        printLog(message = "$article")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setComposeView()
    }

    private fun setComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NewsDetailView(item = article)
            }
        }
    }
}