package com.example.dr3_e2_a1

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseFunctions {

    val dbFire = Firebase.firestore

    fun addContact(){

    }

    fun getCurrentUser(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}