package com.example.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipeapp.databinding.FavoriteItemBinding
import com.squareup.picasso.Picasso

class FavoritePagerItemFragment : Fragment() {

    private lateinit var binding: FavoriteItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteItemBinding.inflate(inflater, container, false)
        val root = binding.root

        val details = arguments?.getParcelableArray("recipeDetails")?.filterIsInstance<RecipeInformation>()
        val position = arguments?.getInt("position") ?: 0

        displayRecipeDetails(details, position)

        return root
    }

    private fun displayRecipeDetails(details: List<RecipeInformation>?, position: Int) {
        if (details.isNullOrEmpty() || position < 0 || position >= details.size) {
            // Handle invalid or empty details array or position here
            return
        }

        binding.favoriteName.text = details[position].title
        Picasso.get().load(details[position].image).into(binding.favoriteImage)
    }
}