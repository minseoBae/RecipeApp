package com.example.recipeapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteViewModel : ViewModel() {
    // 데이터 정의
    private val _userAccount = MutableLiveData<UserAccount?>()
    val userAccount: MutableLiveData<UserAccount?> = _userAccount

    init {
        // 현재 로그인된 사용자의 정보를 가져온다.
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("UserAccount").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val userAccount = documentSnapshot.toObject(UserAccount::class.java)
                    _userAccount.value = userAccount
                }
        }
    }
}