package com.example.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = binding.root

        profileViewModel.userAccount.observe(viewLifecycleOwner) { account ->
            binding.userName.text = account?.name
            binding.userName1.text = account?.name
            binding.email.text = account?.emailId
            binding.phone.text = account?.phone.toString()
            binding.localdate.text = "Last visit " + account?.lastLoginDate.toString()
            binding.age.text = account?.age.toString()
            binding.address.text = account?.address
        }
        return root
    }
}