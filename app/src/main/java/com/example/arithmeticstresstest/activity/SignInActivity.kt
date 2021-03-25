package com.example.arithmeticstresstest.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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

        binding.signInButton.setOnClickListener {
            singInUser()
        }
    }

    private fun singInUser() {
        val email = binding.editTextEmailAddress.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.editTextEmailAddress.error = "Email is required"
            binding.editTextEmailAddress.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.editTextEmailAddress.error = "Please provide valid email!"
            binding.editTextEmailAddress.requestFocus()
            return
        }

        if (password.isEmpty())
        {
            binding.editTextPassword.error = "Password is required"
            binding.editTextPassword.requestFocus()
            return
        }

        if (password.length < 6)
        {
            binding.editTextPassword.error = "Min password length should be 6 characters!"
            binding.editTextPassword.requestFocus()
            return
        }


        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener{
            if(it.isSuccessful){
                openHomeActivity()
            }else{
                binding.txtErrorMessage.text = "Wrong email or password!"
                binding.txtErrorMessage.requestFocus()
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