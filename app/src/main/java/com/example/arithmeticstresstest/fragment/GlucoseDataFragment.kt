package com.example.arithmeticstresstest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.arithmeticstresstest.databinding.FragmentGlucoseDataBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class GlucoseDataFragment: Fragment() {

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: FragmentGlucoseDataBinding

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGlucoseDataBinding.inflate(inflater, container, false)


        return binding.root
    }

}