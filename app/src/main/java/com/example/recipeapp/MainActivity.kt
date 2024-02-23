package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    // Firebase 인증 객체 초기화
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var searchfill: ImageButton
    private lateinit var recipe: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipe = findViewById(R.id.recipe_button)
        searchfill = findViewById(R.id.search_fill)
        firebaseAuth = FirebaseAuth.getInstance()

        searchfill.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }

        recipe.setOnClickListener {
            startActivity(Intent(this, RecipeListActivity::class.java))
            finish()
        }

        initializeLayout()
    }

    // 로그인 상태를 확인하는 함수
    private fun isLoggedIn(): Boolean {
        val currentUser = firebaseAuth.currentUser
        return currentUser != null
    }

    private fun initializeLayout() {
        // 툴바를 통해 앱 바 생성
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        // 앱 바의 좌측 영역에 드로어를 열기 위한 아이콘 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val menu = navigationView.menu
        val loginMenuItem = menu.findItem(R.id.navigation_login)
        val logoutMenuItem = menu.findItem(R.id.navigation_logout)

        if (isLoggedIn()) {
            loginMenuItem.setTitle(R.string.navigation_profile)
            loginMenuItem.setIcon(R.drawable.user)
            logoutMenuItem.isVisible = true
            loginMenuItem.setOnMenuItemClickListener {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            logoutMenuItem.setOnMenuItemClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
        } else {
            loginMenuItem.setTitle(R.string.navigation_login)
            logoutMenuItem.isVisible = false
            loginMenuItem.setOnMenuItemClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
        }

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            androidx.navigation.ui.R.string.nav_app_bar_open_drawer_description,
            androidx.compose.ui.R.string.close_drawer
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }
        }

        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // 네비게이션 뷰 객체에 이벤트 리스너 설정
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                        startActivity(Intent(this, MainActivity::class.java))
                        true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                R.id.navigation_favorite -> {
                    Toast.makeText(applicationContext, "SelectedItem 3", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}