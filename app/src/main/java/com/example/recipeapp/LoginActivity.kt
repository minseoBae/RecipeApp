package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth // 파이어베이스 인증
    private lateinit var database: FirebaseFirestore // firestore 데이터 베이스
    private lateinit var etEmail: EditText
    private lateinit var etPwd: EditText
    private lateinit var btnLogin: ImageButton
    private lateinit var btnRegister: Button
    private lateinit var btnBack: ImageButton
    private lateinit var now: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        now = LocalDateTime.now()

        etEmail = findViewById(R.id.et_email)
        etPwd = findViewById(R.id.et_pwd)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        btnBack = findViewById(R.id.btn_back)

        btnLogin.setOnClickListener {
            // 로그인 버튼 클릭
            val email = etEmail.text.toString()
            val password = etPwd.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공
                        // 최근 로그인 날짜 최신화
                        val now = LocalDateTime.now()

                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        val userAccountRef = FirebaseFirestore.getInstance().collection("UserAccount").document(uid)
                        userAccountRef.update(mapOf("lastLoginDate" to now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // 로그인 실패
                        task.exception?.message?.let { message ->
                            Toast.makeText(this, "아이디나 비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        btnRegister.setOnClickListener {
            // 회원가입 버튼 클릭
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}