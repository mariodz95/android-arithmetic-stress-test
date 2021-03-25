package com.example.arithmeticstresstest.activity.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.activity.CreateAccountActivity
import com.example.arithmeticstresstest.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        val button: Button = root.findViewById(R.id.btnLogout)
        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            openMainActivity()
        }
        return root
    }


    private fun openMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
}