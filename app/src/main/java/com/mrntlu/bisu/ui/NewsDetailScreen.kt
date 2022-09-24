package com.mrntlu.bisu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.bisu.databinding.FragmentNewsDetailScreenBinding

class NewsDetailScreen : BaseFragment<FragmentNewsDetailScreenBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}