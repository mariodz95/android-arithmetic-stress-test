package com.example.arithmeticstresstest.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.activity.HomeActivity
import com.example.arithmeticstresstest.activity.SettingsActivity
import com.example.arithmeticstresstest.databinding.FragmentMyProfileBinding
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.ProfileData
import com.example.arithmeticstresstest.model.ProfileViewModel
import com.example.arithmeticstresstest.model.ProfileViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
     private var mAuth: FirebaseAuth? = null

    private val repository : FirebaseRepository by inject()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentMyProfileBinding

    var date: Date? = null
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        var factory = ProfileViewModelFactory(repository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        profileViewModel.getProfileData(mAuth?.currentUser?.uid)

        profileViewModel.profileData?.observe(viewLifecycleOwner, Observer {
            binding.editTextName.setText(it.name)
            binding.editTestLastName.setText(it.lastName)
            binding.editTextTypeOfDiabetes.setText(it.typeOfDiabetes)
            binding.editTextWeight.setText(it.weight.toString())

            val format = SimpleDateFormat("yyyy/MM/dd")
            var formatedDate = format.format(it.dateOfBirth)

            binding.editTextDateOfBirth.setText("$formatedDate")
        })

        binding.btnUpdateProfile.setOnClickListener{
            submitProfileData()
        }

        binding.editTextDateOfBirth.setOnClickListener{
            showDatePickerDialog()
        }

        return binding.root
    }

    private fun submitProfileData(){
        val name = binding.editTextName.text.toString().trim()
        val lastName = binding.editTestLastName.text.toString().trim()
        val typeOfDiabetes = binding.editTextTypeOfDiabetes.text.toString().trim()

        val weight = binding.editTextWeight.text.toString().trim()
        val dateOfBirth = binding.editTextDateOfBirth.text.toString().trim()

        val profileData = ProfileData(name, lastName, typeOfDiabetes, weight.toFloat(), Date(dateOfBirth))
        profileViewModel.saveProfileData(mAuth!!.currentUser.uid, profileData)
        openHomeActivity()
    }

    private fun openHomeActivity() {
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showDatePickerDialog() {
        var dialog = DatePickerFragment()

        dialog.setOnChangeListener(object : DatePickerFragment.OnChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onChange(year: Int, month: Int, day: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                date = calendar.time

                val format = SimpleDateFormat("yyyy/MM/dd")
                var formatedDate = format.format(date)

                binding.editTextDateOfBirth.setText("$formatedDate")
            }
        })
        dialog.show(requireFragmentManager(), "datePicker")
    }
}