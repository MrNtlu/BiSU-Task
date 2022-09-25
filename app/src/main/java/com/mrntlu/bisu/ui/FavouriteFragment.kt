package com.mrntlu.bisu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.bisu.databinding.FragmentFavouriteBinding

class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}