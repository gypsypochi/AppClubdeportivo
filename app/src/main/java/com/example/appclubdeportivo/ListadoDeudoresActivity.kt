package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView

class ListadoDeudoresActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        dbHelper = ClubDBHelper(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_deudores)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }

        /* Boton Cerrar */
        val btnClose: Button =findViewById(R.id.btnClose)
        btnClose.setOnClickListener{
            finish()
        }

        listDeudores()
    }

    private fun listDeudores(){
        val listView = findViewById<ListView>(R.id.listDeudores)
        val list = dbHelper.getDeudores()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }
}