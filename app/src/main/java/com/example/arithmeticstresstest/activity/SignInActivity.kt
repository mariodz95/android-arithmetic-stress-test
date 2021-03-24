package com.example.arithmeticstresstest.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.arithmeticstresstest.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Log.v("test"," currenmt user, ${mAuth!!.currentUser.email}")

        binding.signInButton.setOnClickListener {
            singInUser()
        }
    }

    private fun singInUser() {
        val email = binding.editTextEmailAddress.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener{
            if(it.isSuccessful){
                openHomeActivity()
            }
        }
    }

    private fun openHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if(currentUser != null){
            openHomeActivity()
        }
    }
}