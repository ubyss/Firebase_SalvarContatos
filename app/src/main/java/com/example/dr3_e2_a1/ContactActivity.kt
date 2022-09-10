package com.example.dr3_e2_a1

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_contact.*


class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
//        -------------------------------------------------------------------------------
        val dbFire = Firebase.firestore
        val currentUser = FirebaseFunctions().getCurrentUser()

        var name = intent.getStringExtra("name")
        var phone = intent.getStringExtra("phone")
        var id = intent.getStringExtra("id")

        dbFire.collection("/${currentUser}")
            .document("$id")
            .get()
            .addOnSuccessListener { document ->
                txtContactName.setText(document.data?.get("name").toString())
                txtContactFone.setText(document.data?.get("phone").toString())
            }
            .addOnFailureListener { exception ->
                Log.w("getdata", "Error getting documents: ", exception)
            }

//        -------------------------------------------------------------------------------




        btnAtualizar.setOnClickListener {
            dbFire.collection("/${currentUser}")
                .document("$id")
                .update(
                    "name", txtContactName.text.toString(),
                    "phone",txtContactFone.text.toString()
                )

                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Dado atualizado com sucesso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }


        }

        btnDelete.setOnClickListener {
            dbFire.collection("/${currentUser}").document("$id")
                .delete()
                .addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                .addOnFailureListener { e -> Log.w("Firebase", "Error deleting document", e) }
        }

    }
}