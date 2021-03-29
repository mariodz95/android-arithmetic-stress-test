package com.example.arithmeticstresstest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.ActivityResultBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.TestScore
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.util.*

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        val userResult: Int = intent.getIntExtra("TEST_RESULT", 0)
        val numberOfCalculations: Int = intent.getIntExtra("NUMBER_OF_CALCULATIONS", 0)

        val currentTime: Date = Calendar.getInstance().time

        val testScore = TestScore(userResult, numberOfCalculations, currentTime )
        dataViewModel.insertTestScoreResult(testScore, mAuth?.currentUser!!.uid)

        binding.txtResult.text = "$userResult / $numberOfCalculations "

        binding.btnBackToHome.setOnClickListener {
            openHomeActivity()
        }

        binding.btnInsertData.setOnClickListener{
            openInsertDataActivity()
        }
    }

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun openInsertDataActivity() {
        val intent = Intent(this, InsertDataActivity::class.java)
        intent.putExtra("TYPE", "AfterTest")
        startActivity(intent)
    }
}