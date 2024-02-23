package com.example.recipeapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ExRecipeAdapter(private val context: Context, private val recipes: MutableList<ExtractionRecipe>) :
    RecyclerView.Adapter<ExRecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewName)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewIngredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.optional_recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        Picasso.get().load(recipe.image).into(holder.imageView)

        if(position != RecyclerView.NO_POSITION) {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, RecipeDetailActivity::class.java)
                intent.putExtra("data", recipe)
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}