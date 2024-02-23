package com.example.recipeapp

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.recipeapp.databinding.FragmentModifyBinding

class ModifyViewModel : ViewModel() {
    private val _userAccount = MutableLiveData<UserAccount?>()
    val userAccount: MutableLiveData<UserAccount?> = _userAccount

    private val _imageProfile = MutableLiveData<Bitmap>()
    val imageProfile: LiveData<Bitmap> = _imageProfile

    private lateinit var requireContext: Context
    private lateinit var binding: FragmentModifyBinding

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

    fun saveProfile() {
        val username = binding.userName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val phone = binding.Phone.text.toString().trim()
        val address = binding.address.text.toString().trim()
        val age = Integer.parseInt(binding.age.text.toString().trim())

        // UserAccount 객체 생성
        val userAccount = UserAccount(username, email, password, phone, address, age)

        // MutableLiveData 프로퍼티의 값을 업데이트한다.
        _userAccount.value = userAccount

        // 현재 로그인된 사용자의 UID 가져오기
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // 사용자 정보 문서 가져오기
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("UserAccount").document(uid)
                .set(userAccount)
                .addOnSuccessListener {
                    // 업데이트 성공 메시지 표시
                    Toast.makeText(requireContext, "프로필 정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // 에러 메시지 표시
                    Toast.makeText(requireContext, "프로필 정보 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun setImageProfile(bitmap: Bitmap) {
        _imageProfile.value = bitmap
    }
}
