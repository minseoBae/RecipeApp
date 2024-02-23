package com.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FavoritePagerAdapter(fragment: Fragment, private val details: List<RecipeInformation>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = details.size

    override fun createFragment(position: Int): Fragment {
        val fragment = FavoritePagerItemFragment()
        val bundle = Bundle()
        bundle.putParcelable("recipeDetails", details[position])
        bundle.putInt("position", position)
        fragment.arguments = bundle
        return fragment
    }
}