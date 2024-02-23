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


class RecipeAdapter(private val context: Context, private val recipes: MutableList<SearchedRecipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewName)
        val likes: TextView = itemView.findViewById(R.id.textViewLikes)
        val useCount: TextView = itemView.findViewById(R.id.textViewUsed)
        val unUseCount: TextView = itemView.findViewById(R.id.textViewUnUsed)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewIngredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        holder.likes.text = recipe.likes.toString() + "♥"
        holder.useCount.text = "사용한 재료 수" + recipe.usedIngredientCount.toString()
        holder.unUseCount.text = "사용 안한 재료 수" +  recipe.missedIngredientCount.toString()
        Picasso.get().load(recipe.image).into(holder.imageView)

        if(position != RecyclerView.NO_POSITION) { // 현재 위치가 리사이클러뷰의 아이템 위치이면
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