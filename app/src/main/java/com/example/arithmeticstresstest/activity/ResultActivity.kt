package com.example.arithmeticstresstest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arithmeticstresstest.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userResult: Int = intent.getIntExtra("TEST_RESULT", 0)
        val numberOfCalculations: Int = intent.getIntExtra("NUMBER_OF_CALCULATIONS", 0)

        binding.txtResult.text = "$userResult / $numberOfCalculations "

        binding.btnBackToHome.setOnClickListener {
            openHomeActivity()
        }
    }

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}