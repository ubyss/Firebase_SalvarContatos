package com.example.dr3_e2_a1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dr3_e2_a1.roomContactInfo.ContatoInfo
import com.example.dr3_e2_a1.roomContactInfo.ContatoInfoDao
import com.example.dr3_e2_a1.roomContato.Contato
import com.example.dr3_e2_a1.roomContato.ContatoDao
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.grpc.InternalChannelz.id
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var arrayTeste = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.atualizarLista()

        btnGravar.setOnClickListener{
            val dbFire = Firebase.firestore
            val currentUser = FirebaseFunctions().getCurrentUser()


            if(txtNome.text.isEmpty() || txtFone.text.isEmpty()){
                Toast.makeText(this@MainActivity, "Não é possível salvar dados em branco", Toast.LENGTH_LONG).show()
            } else {
                val contact = hashMapOf(
                    "name" to txtNome.text.toString(),
                    "phone" to txtFone.text.toString()
                )
                dbFire.collection("/${currentUser}")
                    .add(contact)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding document", e)
                    }

                this.atualizarLista()


                txtNome.text = null
                txtFone.text = null

                }
        }

        this.findViewById<ListView>(R.id.listView).setOnItemClickListener { adapterView, view, i, l ->

            val intent = Intent(this, ContactActivity::class.java)
            intent.putExtra("name", arrayTeste[i].name.toString())
            intent.putExtra("phone", arrayTeste[i].phone.toString())
            intent.putExtra("id", arrayTeste[i].id.toString())
            startActivity(intent)
        }
    }

    private fun atualizarLista(){

        val dbFire = Firebase.firestore
        val dados = ArrayList<String>()

        dbFire.collection(FirebaseFunctions().getCurrentUser())
            .get()
            .addOnSuccessListener {

                for (document in it) {

                    arrayTeste.add(
                        Data(
                            document.id,
                            document.data["name"].toString(),
                            document.data["phone"].toString()
                        )
                    )

                    dados.add(document.data["name"].toString())

                    val lstContato = this.findViewById<ListView>(R.id.listView)
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1, dados
                    )
                    lstContato.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}