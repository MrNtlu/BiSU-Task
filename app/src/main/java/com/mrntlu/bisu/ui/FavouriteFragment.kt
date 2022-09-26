package com.mrntlu.bisu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.bisu.R
import com.mrntlu.bisu.adapter.FavouritesAdapter
import com.mrntlu.bisu.databinding.FragmentFavouriteBinding
import com.mrntlu.bisu.interfaces.Interaction
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.util.isInternetAvailable
import com.mrntlu.bisu.viewmodel.FavouritesViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {

    private val viewModel: FavouritesViewModel by viewModels()
    private var favsAdapter: FavouritesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObserver()
    }

    private fun setRecyclerView() {
        binding.favsRV.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
            favsAdapter = FavouritesAdapter(object: Interaction<Article> {
                override fun onItemSelected(position: Int, item: Article) {
                    val bundle = item.toBundle()
                    navController.navigate(R.id.action_navigation_favourites_to_newsDetailScreen, bundle)
                }

                override fun onFavTogglePressed(position: Int, item: Article, isFavAdded: Boolean) {
                    favsAdapter?.submitRemoveItem(item)
                    viewModel.deleteFavouriteNews(item.url)
                }
            })
            adapter = favsAdapter
        }
    }

    private fun setObserver() {
        viewModel.getAllFavouritesAndSetListener().observe(viewLifecycleOwner) { favourites ->
            if (isInternetAvailable(binding.root.context)) {
                favsAdapter?.submitList(favourites)
            } else {
                favsAdapter?.submitError("No internet connection!")
            }
        }
    }

    override fun onDestroyView() {
        favsAdapter = null
        viewModel.onDestroy()
        super.onDestroyView()
    }
}