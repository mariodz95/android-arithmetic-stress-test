package com.example.arithmeticstresstest.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.FragmentMyProfileBinding
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.ProfileViewModel
import com.example.arithmeticstresstest.model.ProfileViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {
     private var mAuth: FirebaseAuth? = null

    private val repository : FirebaseRepository by inject()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        var factory = ProfileViewModelFactory(repository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        profileViewModel.getProfileData(mAuth?.currentUser?.uid)

        profileViewModel.profileData?.observe(viewLifecycleOwner, Observer {
                Log.v("sada", "It bi trebao biti null $it")

        })

        binding.btnUpdateProfile.setOnClickListener{
            submitProfileData()
        }

        return binding.root
    }

    private fun submitProfileData(){
        val name = binding.editTextName.text.toString().trim()
        val lastName = binding.editTestLastName.text.toString().trim()
        val typeOfDiabetes = binding.editTextTypeOfDiabetes.text.toString().trim()
        val weight = binding.editTextWeight.text.toString().trim()
        val dateOfBirth = binding.editTextDateOfBirth.text.toString().trim()



    }
}