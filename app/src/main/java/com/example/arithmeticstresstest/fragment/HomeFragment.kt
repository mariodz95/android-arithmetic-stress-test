package com.example.arithmeticstresstest.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.activity.InsertDataActivity
import com.example.arithmeticstresstest.activity.ResultActivity
import com.example.arithmeticstresstest.databinding.FragmentHomeBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.StressTestResult
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.util.*


private const val START_TIME_IN_MILLIS = 420000
private const val START_NUMBER_TIME_IN_MILLIS = 7000

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var timeLeftInMillis : Long= START_TIME_IN_MILLIS.toLong()
    private var timeLeftInMillisForNumber : Long= START_NUMBER_TIME_IN_MILLIS.toLong()

    private var countDownTimer: CountDownTimer? = null
    private var smallCountDownTimer: CountDownTimer? = null
    private var generateNumberCountDownTime: CountDownTimer? = null

    var soundFlag: Boolean = false
    private var numberTimeReset: Long = START_NUMBER_TIME_IN_MILLIS.toLong()

    private var points = 0
    private var numberOfCalculations = 0

    var leftNumber: Int = (100..1000).random()
    var rightNumber: Int = (100..1000).random()

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        binding.startTest.setOnClickListener {
            startTestTimer()
            generateNumbersEveryXSeconds()
            startNumberTimer()
        }

        binding.btnSubmit.setOnClickListener {
            var correctResult: Int = leftNumber - rightNumber
            var myResult = binding.editTxtAnswer.text.toString()
            binding.editTxtAnswer.text.clear()

            when {
                myResult == "" -> {
                    smallCountDownTimer?.cancel()
                    generateNumberCountDownTime?.cancel()
                    smallCountDownTimer?.start()
                    generateNumberCountDownTime?.start()
                    updateNumberCountDownText()

                }
                correctResult == myResult.toInt() -> {
                    points++
                    binding.editTxtAnswer.setText("")
                    smallCountDownTimer?.cancel()
                    generateNumberCountDownTime?.cancel()
                    smallCountDownTimer?.start()
                    generateNumberCountDownTime?.start()
                    updateNumberCountDownText()
                }
                else -> {
                    smallCountDownTimer?.cancel()
                    generateNumberCountDownTime?.cancel()
                    smallCountDownTimer?.start()
                    generateNumberCountDownTime?.start()
                    updateNumberCountDownText()
                }
            }
        }

        binding.fab.setOnClickListener{
            openInsertDataActivity()
        }

        return binding.root
    }

    private fun startTestTimer() {
        val prefs = this.activity?.getSharedPreferences(
                "DURATION_TIME",
                AppCompatActivity.MODE_PRIVATE
        )
        timeLeftInMillis = prefs?.getLong("duration", 180000)!!
        timeLeftInMillis = 20000

        val prefsNumberTime = this.activity?.getSharedPreferences(
                "NUMBER_RESET_TIME",
                AppCompatActivity.MODE_PRIVATE
        )
        timeLeftInMillisForNumber =
            prefsNumberTime?.getLong("numberResetTime", 7000)!!
        numberTimeReset = timeLeftInMillisForNumber

        countDownTimer = object: CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
                updateNumberCountDownText()
            }

            override fun onFinish() {
                val currentTime = Calendar.getInstance().time
                val stressTestResult = StressTestResult(mAuth!!.currentUser.email, points, numberOfCalculations, currentTime)
                dataViewModel.saveTestResult(stressTestResult)
                openResultActivity()
            }
        }.start()
        binding.startTest.visibility = View.GONE
        binding.txtRightNumber.visibility = View.VISIBLE
        binding.txtLefNumber.visibility = View.VISIBLE
        binding.txtSubtract.visibility = View.VISIBLE
        binding.editTxtAnswer.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.VISIBLE
        binding.txtDescription.visibility = View.GONE
        binding.fab.visibility = View.GONE
    }

    private fun updateCountDownText(){
        val minutes : Int = (timeLeftInMillis.toInt() / 1000) / 60
        val seconds : Int = (timeLeftInMillis.toInt() / 1000) % 60
        val timeLeftFormatted : String = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds
        )
        binding.txtTimer.text = timeLeftFormatted
    }

    private fun updateNumberCountDownText(){
        val minutes : Int = (timeLeftInMillisForNumber.toInt() / 1000) / 60
        val seconds : Int = (timeLeftInMillisForNumber.toInt() / 1000) % 60
        val timeLeftFormatted : String = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds
        )
        binding.txtSmallTimer.text = timeLeftFormatted
    }

    private fun generateNumbersEveryXSeconds() {
        generateNumberCountDownTime = object: CountDownTimer(timeLeftInMillisForNumber, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                numberOfCalculations++
                if(soundFlag) {
                    sound()
                }
                val randomNumberOne = (100..1000).random()
                val randomNumberTwo = (100..1000).random()
                leftNumber = randomNumberOne
                rightNumber = randomNumberTwo
                if(randomNumberOne < randomNumberTwo){
                    leftNumber = randomNumberTwo
                    rightNumber = randomNumberOne
                }
                binding.txtLefNumber.text = leftNumber.toString()
                binding.txtRightNumber.text = rightNumber.toString()
                binding.editTxtAnswer.text.clear()
                soundFlag = true
            }
            override fun onFinish() {
            }
        }.start()
    }

    private fun sound(){
        val wrongSound = MediaPlayer.create(activity, R.raw.wrong)
        wrongSound.start()
    }

    private fun startNumberTimer() {
        smallCountDownTimer = object: CountDownTimer(timeLeftInMillisForNumber, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillisForNumber = millisUntilFinished
            }
            override fun onFinish() {
                    timeLeftInMillisForNumber = numberTimeReset
                    startNumberTimer()
                    generateNumbersEveryXSeconds()
            }
        }.start()
    }

    private fun stopTimers(){
        countDownTimer?.cancel()
        countDownTimer = null

        smallCountDownTimer?.cancel()
        smallCountDownTimer = null

        generateNumberCountDownTime?.cancel()
        generateNumberCountDownTime = null
    }

    override fun onStop() {
        stopTimers()
        super.onStop()
    }

    private fun openResultActivity() {
        stopTimers()
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra("TEST_RESULT", points)
        intent.putExtra("NUMBER_OF_CALCULATIONS", numberOfCalculations)
        startActivity(intent)
    }

    private fun openInsertDataActivity() {
        stopTimers()
        val intent = Intent(activity, InsertDataActivity::class.java)
        intent.putExtra("TYPE", "BeforeTest")
        startActivity(intent)
    }
}