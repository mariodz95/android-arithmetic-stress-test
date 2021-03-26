package com.example.arithmeticstresstest.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.activity.ResultActivity
import com.example.arithmeticstresstest.activity.ui.home.HomeViewModel
import com.example.arithmeticstresstest.databinding.FragmentHomeBinding
import java.util.*


private const val START_TIME_IN_MILLIS = 420000
private const val START_NUMBER_TIME_IN_MILLIS = 7000

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var timeLeftInMillis : Long= START_TIME_IN_MILLIS.toLong()
    private var timeLeftInMillisForNumber : Long= START_NUMBER_TIME_IN_MILLIS.toLong()

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var smallCountDownTimer: CountDownTimer
    private lateinit var generateNumberCountDownTime: CountDownTimer

    private var numberTimeReset: Long = START_NUMBER_TIME_IN_MILLIS.toLong()

    private var points = 0
    private var numberOfCalculations = 0

    var leftNumber: Int = (100..1000).random()
    var rightNumber: Int = (100..1000).random()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.startTest.setOnClickListener {
            startTestTimer()
            generateNumbersEveryXSeconds()
            startNumberTimer()
        }

        binding.btnSubmit.setOnClickListener {
            var correctResult: Int = leftNumber - rightNumber
            var myResult = binding.editTxtAnswer.text.toString()

            when {
                myResult == "" -> {
                    smallCountDownTimer.cancel()
                    generateNumberCountDownTime.cancel()
                    smallCountDownTimer.start()
                    generateNumberCountDownTime.start()
                    updateNumberCountDownText()

                }
                correctResult == myResult.toInt() -> {
                    points++
                    binding.editTxtAnswer.setText("")
                    smallCountDownTimer.cancel()
                    generateNumberCountDownTime.cancel()
                    smallCountDownTimer.start()
                    generateNumberCountDownTime.start()
                    updateNumberCountDownText()
                }
                else -> {
                    smallCountDownTimer.cancel()
                    generateNumberCountDownTime.cancel()
                    smallCountDownTimer.start()
                    generateNumberCountDownTime.start()
                    updateNumberCountDownText()
                }
            }
        }

        return binding.root
    }

    private fun startTestTimer() {
        val prefs = this.activity?.getSharedPreferences(
            "DURATION_TIME",
            AppCompatActivity.MODE_PRIVATE
        )
        timeLeftInMillis = prefs?.getLong("duration", 180000)!!

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
                openResultActivity()
            }
        }.start()
        binding.startTest.visibility = View.GONE
        binding.txtRightNumber.visibility = View.VISIBLE
        binding.txtLefNumber.visibility = View.VISIBLE
        binding.txtSubtract.visibility = View.VISIBLE
        binding.editTxtAnswer.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.VISIBLE
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
        numberOfCalculations++
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
            }
            override fun onFinish() {

            }
        }.start()
    }

    private fun startNumberTimer() {
        smallCountDownTimer = object: CountDownTimer(timeLeftInMillisForNumber, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillisForNumber = millisUntilFinished
                updateNumberCountDownText()
            }
            override fun onFinish() {
                    timeLeftInMillisForNumber = numberTimeReset
                    startNumberTimer()
                    generateNumbersEveryXSeconds()
            }
        }.start()
    }


    private fun openResultActivity() {
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra("TEST_RESULT", points)
        intent.putExtra("NUMBER_OF_CALCULATIONS", numberOfCalculations)
        startActivity(intent)
    }

}