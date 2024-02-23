package com.example.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.recipeapp.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root

        // Initialize ViewModel
        favoriteViewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]

        // Observe changes in the list of favorite recipe IDs
        favoriteViewModel.userAccount.observe(viewLifecycleOwner) { userAccount ->
            userAccount?.favoriteRecipesId?.let { favoriteRecipeIds ->
                // Fetch details for each favorite recipe ID
                fetchFavoriteRecipesDetails(favoriteRecipeIds)
            }
        }

        return root
    }



    private fun fetchFavoriteRecipesDetails(recipeIds: List<Int>) {
        lifecycleScope.launch {
            val details = mutableListOf<RecipeInformation>()
            for (recipeId in recipeIds) {
                val detail = withContext(Dispatchers.IO) {
                    ApiClient.getRecipeInformation(recipeId)
                }
                if (detail != null) {
                    details.add(detail)
                } // Add the fetched RecipeInformation to the list
            }
            setupViewPager(details)
        }
    }

    private fun setupViewPager(details: List<RecipeInformation>) {
        val viewPager: ViewPager2 = binding.viewpager

        val adapter = FavoritePagerAdapter(this, details)
        viewPager.adapter = adapter
    }
}