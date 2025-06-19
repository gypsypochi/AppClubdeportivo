package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ListadoNoSociosActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_no_socios)

        dbHelper = ClubDBHelper(this)

        listNonMembers()

        /*arrow back*/
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /*Boton Cerrar*/
        val btnClose: Button =findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }

    }

    private fun listNonMembers(){
        val listView = findViewById<ListView>(R.id.listNoSocios)
        val list = dbHelper.listNoSocios()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }
}