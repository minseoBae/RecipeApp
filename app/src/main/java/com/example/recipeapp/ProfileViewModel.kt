package com.example.recipeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.util.Base64Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    // 데이터 정의
    private val _userAccount = MutableLiveData<UserAccount?>()
    val userAccount: MutableLiveData<UserAccount?> = _userAccount
    // 이미지 뷰에 표시할 사진 데이터
    private val _imageProfile = MutableLiveData<Bitmap?>()
    val imageProfile: MutableLiveData<Bitmap?> = _imageProfile

    // ViewModel이 생성될 때 현재 로그인 된 사용자 정보를 가져옵니다.
    init {
        // 현재 로그인한 사용자의 UID를 가져옵니다.
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // UID를 사용하여 Firestore에서 사용자 정보를 가져옵니다.
        val userAccountRef = uid?.let {
            FirebaseFirestore.getInstance().collection("UserAccount").document(
                it
            )
        }
        userAccountRef?.get()?.addOnSuccessListener { document ->
            val userAccount = document.toObject(UserAccount::class.java)
            _userAccount.value = userAccount

            // 사진 데이터가 있으면
            if (userAccount?.imageProfile != null) {
                // 사진 데이터를 바이트 배열로 변환합니다.
                val byteArray = Base64Utils.decode(userAccount.imageProfile.toString())

                // 바이트 배열을 비트맵으로 변환합니다.
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                // 비트맵을 이미지 뷰에 표시합니다.
                _imageProfile.value = bitmap
            }
        }
    }
}