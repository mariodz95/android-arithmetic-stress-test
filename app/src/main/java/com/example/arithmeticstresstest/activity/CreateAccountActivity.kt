package com.example.arithmeticstresstest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.ActionBar
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.actionbar_layout);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.btnCreateAccount.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val email = binding.editTextEmailAddress.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val passwordConfirmation = binding.editTextPasswordConfirmation.text.toString().trim()

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

        if(password != passwordConfirmation){
            binding.editTextPasswordConfirmation.error = "Your passwords must match"
            binding.editTextPassword.requestFocus()
            return
        }

        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                openHomeActivity()
            }else{
                binding.txtErrorMessage.text = "Account already exist!"
                binding.txtErrorMessage.requestFocus()
            }
        }
    }

    private fun openHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}