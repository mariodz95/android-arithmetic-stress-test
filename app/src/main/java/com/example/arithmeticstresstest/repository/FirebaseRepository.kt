package com.example.arithmeticstresstest.repository

import com.example.arithmeticstresstest.model.TestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseRepository {
    fun insertBeforeOrAfterTest(testData: TestData, uid: String?){
        val db = Firebase.firestore
        db.collection("testMeasurement ").document(uid!!).collection("measurement").add(testData)
    }
}