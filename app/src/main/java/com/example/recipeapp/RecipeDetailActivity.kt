package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailActivity : AppCompatActivity() {
    private val ingredient = mutableListOf<RecipeDetail>()
    private val similarRecipe = mutableListOf<SimilarRecipe>()
    private val guideline = mutableListOf<GuideLine>()
    private lateinit var btnBack: ImageButton
    private lateinit var btnFavorite: ImageButton
    private lateinit var firestore: FirebaseFirestore
    private var addToFavorite: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        btnBack = findViewById(R.id.btn_back4)
        btnFavorite = findViewById(R.id.btnFavorite)
        firestore = FirebaseFirestore.getInstance()

        // 전달받은 인텐트 데이터를 읽어와서 화면에 표시하기
        val datas: Any = intent.getSerializableExtra("data") ?: return

        when (datas) {
            is SearchedRecipe -> {
                val recipeId = datas.id
                addToFavorite = recipeId
                fetchDetails(recipeId)
                findViewById<TextView>(R.id.p_title).text = datas.title
                Picasso.get().load(datas.image).into(findViewById<ImageView>(R.id.p_photo))
            }
            is ExtractionRecipe -> {
                val exRecipeId = datas.id
                addToFavorite = exRecipeId
                fetchDetails(exRecipeId)
                findViewById<TextView>(R.id.p_title).text = datas.title
                Picasso.get().load(datas.image).into(findViewById<ImageView>(R.id.p_photo))
            }
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val userDocRef = uid?.let { firestore.collection("UserAccount").document(it) }

        userDocRef?.get()?.addOnSuccessListener { documentSnapshot ->
            val userAccount = documentSnapshot.toObject(UserAccount::class.java)
            val favoriteRecipes = userAccount?.favoriteRecipesId ?: listOf()

            // Check if the current recipe ID is in the user's favorite recipes
            val recipeIdToAdd = when (datas) {
                is SearchedRecipe -> (datas as SearchedRecipe).id
                is ExtractionRecipe -> (datas as ExtractionRecipe).id
                else -> null
            }

            if (recipeIdToAdd != null && favoriteRecipes.contains(recipeIdToAdd)) {
                // Recipe ID is in favorites, set the button to fullheart
                btnFavorite.setBackgroundResource(R.drawable.fullheart)
            } else {
                // Recipe ID is not in favorites, set the button to emptyheart
                btnFavorite.setBackgroundResource(R.drawable.emptyheart)
            }
        }

        btnFavorite.setOnClickListener {
            addToFavorite?.let { recipeId ->
                toggleFavorite(recipeId)
            }
        }


        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun toggleFavorite(recipeId: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val userDocRef = uid?.let { firestore.collection("UserAccount").document(it) }

        if (uid != null) {
            FirebaseFirestore.getInstance().collection("UserAccount").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val existingUserAccount = documentSnapshot.toObject(UserAccount::class.java)
                    existingUserAccount?.let {
                        val favoriteRecipes = it.favoriteRecipesId ?: mutableListOf()

                        if (favoriteRecipes.contains(recipeId)) {
                            favoriteRecipes.remove(recipeId)
                            it.favoriteRecipesId = favoriteRecipes
                        } else {
                            favoriteRecipes.add(recipeId)
                            it.favoriteRecipesId = favoriteRecipes
                        }

                        // Update the changes in Firestore
                        userDocRef?.set(it)?.addOnSuccessListener {
                            // Update UI based on the updated list in Firestore
                            if (favoriteRecipes.contains(recipeId)) {
                                btnFavorite.setBackgroundResource(R.drawable.fullheart)
                            } else {
                                btnFavorite.setBackgroundResource(R.drawable.emptyheart)
                            }
                        }?.addOnFailureListener {
                            // Handle failure
                        }
                    }
                }
        }
    }

    private fun fetchDetails(recipeId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val ingredientsResponse = ApiClient.getDetailById(recipeId)
                val similarRecipeResponse = ApiClient.getSimilarRecipeById(recipeId)
                val guideLineResponse = ApiClient.getGuideLineById(recipeId)

                ingredient.clear() // Clear previous data
                ingredient.addAll(ingredientsResponse)
                similarRecipe.clear()
                similarRecipe.addAll(similarRecipeResponse)
                guideline.clear()
                guideline.addAll(guideLineResponse)

                withContext(Dispatchers.Main) {
                    updateRecyclerView()
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun updateRecyclerView() {
        val ingredientRecyclerView = findViewById<RecyclerView>(R.id.ingredientRecyclerView)
        val guidelineRecyclerView = findViewById<RecyclerView>(R.id.guidelineRecyclerView)
        val similarRecipeRecyclerView = findViewById<RecyclerView>(R.id.similarRecipeRecyclerView)

        ingredientRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        guidelineRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        similarRecipeRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val ingredientAdapter = IngredientAdapter(ingredient)
        val guidelineAdapter = GuidelineAdapter(guideline)
        val similarRecipeAdapter = SimilarRecipeAdapter(similarRecipe)

        ingredientRecyclerView.adapter = ingredientAdapter
        guidelineRecyclerView.adapter = guidelineAdapter
        similarRecipeRecyclerView.adapter = similarRecipeAdapter
    }
}