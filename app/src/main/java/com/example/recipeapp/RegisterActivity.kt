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

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth // 파이어 베이스 인증
    private lateinit var database: FirebaseFirestore // firestore 데이터 베이스
    private lateinit var etpwd: EditText
    private lateinit var etpwdConfirm: EditText
    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var btnRegister: ImageButton
    private lateinit var btnLogin: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        etEmail = findViewById(R.id.et_email)
        etpwd = findViewById(R.id.et_pwd)
        etpwdConfirm = findViewById(R.id.et_pwd_confirm)
        etName = findViewById(R.id.et_name)
        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        btnBack = findViewById(R.id.btn_back2)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            //회원가입 처리 시작
            val strEmail = etEmail.text.toString()
            val strPwd = etpwd.text.toString()
            val strConfirm = etpwdConfirm.text.toString()
            val strName = etName.text.toString()

            if(strPwd != strConfirm) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Auth 진행
            firebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 회원가입 성공
                        val firebaseUser = task.result?.user
                        if (firebaseUser != null) {
                            // 회원 정보 저장
                            val account = UserAccount()
                            account.idToken = firebaseUser.uid
                            account.emailId = firebaseUser.email
                            account.password = strPwd
                            account.name = strName

                            val userAccountRef =
                                database.collection("UserAccount").document(firebaseUser.uid)
                            userAccountRef.set(account)

                            // 성공 메시지
                            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                            // 다음 화면으로 이동
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    } else {
                        // 회원가입 실패
                        task.exception?.let {
                            Toast.makeText(this, "회원가입에 실패했습니다. ${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }
    }
}