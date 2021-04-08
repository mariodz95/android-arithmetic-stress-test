package com.example.arithmeticstresstest.fragment

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.BuildConfig
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.activity.HomeActivity
import com.example.arithmeticstresstest.databinding.FragmentMyProfileBinding
import com.example.arithmeticstresstest.model.*
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {
     private var mAuth: FirebaseAuth? = null

    private val repository : FirebaseRepository by inject()
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentMyProfileBinding

    private lateinit var dataViewModel: DataViewModel

    var glucoseLevels: List<GlucoseLevel>? = null

    var path: String? = null

    var date: Date? = null
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        createNotificationChannel()

        var factory = ProfileViewModelFactory(repository)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        profileViewModel.getProfileData(mAuth?.currentUser?.uid)

        var dataViewModelFactory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, dataViewModelFactory)[DataViewModel::class.java]

        dataViewModel.getGlucoseLevels(mAuth?.currentUser?.uid)

        binding.btnGeneratePdf.isEnabled = false

        dataViewModel.glucoseLevels?.observe(viewLifecycleOwner, Observer {
            binding.btnGeneratePdf.isEnabled = true
            glucoseLevels = it
        })

        profileViewModel.profileData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.editTextName.setText(it.name)
                binding.editTestLastName.setText(it.lastName)
                binding.editTextTypeOfDiabetes.setText(it.typeOfDiabetes)
                binding.editTextWeight.setText(it.weight.toString())

                val format = SimpleDateFormat("yyyy/MM/dd")
                var formatedDate = format.format(it.dateOfBirth)

                binding.editTextDateOfBirth.setText("$formatedDate")
            }
        })

        binding.btnUpdateProfile.setOnClickListener{
            submitProfileData()
        }

        binding.editTextDateOfBirth.setOnClickListener{
            showDatePickerDialog()
        }

        verifyStoragePermissions()

        binding.btnGeneratePdf.setOnClickListener{
            generatePdf()
        }


        return binding.root
    }

    private fun generatePdf(){
        // create a new document
        val document = PdfDocument()
        val myPaint: Paint = Paint()

        // create a page description
        val pageInfo = PdfDocument.PageInfo.Builder(250, 400, 1).create()
        val page: PdfDocument.Page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        myPaint.textAlign = Paint.Align.CENTER
        myPaint.textSize = 12f
        canvas.drawText("Glucose levels", pageInfo.pageWidth / 2f, 40f, myPaint)

        val xOs = 110f
        var yOs = 60f

        myPaint.textSize = 8f

        val format = SimpleDateFormat("yyyy/MM/dd hh:mm")

        for(glucose in glucoseLevels!!){
            var formatedDate = format.format(glucose.date)
            canvas.drawText(
                "Glucose: ${glucose.glucoseLevel} | Type: ${glucose.glucoseType} | Date: $formatedDate",
                xOs,
                yOs,
                myPaint
            )
            yOs += 20f
        }

        // finish the page
        document.finishPage(page)

        val file: File = File(Environment.getExternalStorageDirectory(), "/Glucose.pdf")
        path = file.path
        // write the document content
        document.writeTo(FileOutputStream(file))

        // close the document
        document.close()
        pushNotification()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun pushNotification(){

        // Create an explicit intent for an Activity in your app
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        val file = File(path)

        val uri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID.toString() + ".provider",
            file
        )

        intent.setDataAndType(uri, "application/pdf")

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        var builder = NotificationCompat.Builder(requireActivity(), "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_arrow_back_24)
                .setContentTitle("Generated glucose pdf file")
                .setContentText("Click here to open it!")
                .setPriority(NotificationCompat.PRIORITY_HIGH )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)

        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }

    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun submitProfileData(){
        val name = binding.editTextName.text.toString().trim()
        val lastName = binding.editTestLastName.text.toString().trim()
        val typeOfDiabetes = binding.editTextTypeOfDiabetes.text.toString().trim()

        val weight = binding.editTextWeight.text.toString().trim()
        val dateOfBirth = binding.editTextDateOfBirth.text.toString().trim()

        val profileData = ProfileData(
            name, lastName, typeOfDiabetes, weight.toFloat(), Date(
                dateOfBirth
            )
        )
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