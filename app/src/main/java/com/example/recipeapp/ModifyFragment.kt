package com.example.recipeapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.recipeapp.databinding.FragmentModifyBinding
import java.io.IOException

class ModifyFragment : Fragment() {
    private lateinit var binding: FragmentModifyBinding
    private lateinit var viewModel: ModifyViewModel

    private val REQUEST_IMAGE_SELECT = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ModifyViewModel::class.java]

        // Set click listener for selecting profile image
        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }

        // Set click listener for saving profile
        binding.SaveBtn.setOnClickListener {
            saveProfile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                viewModel.setImageProfile(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveProfile() {
        // Get the updated profile information from the input fields
        val username = binding.userName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim() // Ensure the EditText ID matches with the layout
        val phone = binding.Phone.text.toString().trim()
        val address = binding.address.text.toString().trim()
        val age = binding.age.text.toString().toIntOrNull() ?: 0
        val bitmap: Bitmap? = viewModel.imageProfile.value

        // Create a UserAccount object with updated information
        val userAccount = UserAccount(username, email, password, phone, address, age)

        // Get the current user's UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // Update the user's profile information in Firestore
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("UserAccount").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val existingUserAccount = documentSnapshot.toObject(UserAccount::class.java)

                    existingUserAccount?.let { userAccount ->
                        // Update only the modified fields
                        if (username.isNotEmpty()) {
                            userAccount.name = username
                        }
                        if (email.isNotEmpty()) {
                            userAccount.emailId = email
                        }
                        if (password.isNotEmpty()) {
                            userAccount.password = password
                        }
                        if (phone.isNotEmpty()) {
                            userAccount.phone = phone
                        }
                        if (address.isNotEmpty()) {
                            userAccount.address = address
                        }
                        if (true) {
                            userAccount.lastLoginDate = userAccount.lastLoginDate
                        }
            FirebaseFirestore.getInstance().collection("UserAccount").document(uid)
                .set(userAccount)
                .addOnSuccessListener {
                    // Display success message
                    Toast.makeText(requireContext(), "프로필이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStackImmediate()
                }
                .addOnFailureListener {
                    // Display error message
                    Toast.makeText(requireContext(), "프로필 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                    }
                }
        }
    }
}