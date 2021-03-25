package com.example.arithmeticstresstest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.actionbar_layout);
        }

        binding.openSignInBtn.setOnClickListener {
            openSignInActivity()
        }

        binding.openCreateAccountBtn.setOnClickListener {
            openCreateActivityAccount()
        }
    }

    private fun openSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    private fun openCreateActivityAccount() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }
}