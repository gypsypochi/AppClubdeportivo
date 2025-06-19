package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ListadoSociosActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_socios)

        dbHelper = ClubDBHelper(this)

        listMembers()

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Cerrar */
        val btnClose: Button =findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun listMembers(){
        val listView = findViewById<ListView>(R.id.listSocios)
        val list = dbHelper.listSocios()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }
}