package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SimilarRecipeAdapter(private val similarRecipes: List<SimilarRecipe>) :
    RecyclerView.Adapter<SimilarRecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val readyInMinutes: TextView = itemView.findViewById(R.id.textViewReadyInMinutes)
        val servings: TextView = itemView.findViewById(R.id.textViewServings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.similar_recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val similarRecipe = similarRecipes[position]

        holder.title.text = similarRecipe.title
        holder.readyInMinutes.text = "Ready in ${similarRecipe.readyInMinutes} minutes"
        holder.servings.text = "Servings: ${similarRecipe.servings}"

    }

    override fun getItemCount(): Int {
        return similarRecipes.size
    }
}