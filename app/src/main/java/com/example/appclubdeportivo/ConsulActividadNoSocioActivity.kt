package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConsulActividadNoSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consul_actividad_no_socio)

        dbHelper = ClubDBHelper(this)

        id = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""

        /* Arrow Back */
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.nameTextView).text = "No Socio ID: $id - Nombre: $nombre $apellido"

        // Mostrar la Lista
        listActivities()

        /* Boton Cerrar */
        val btnClose: Button =findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }
    }

    // Lista de actividades
    private fun listActivities() {
        val listView = findViewById<ListView>(R.id.listActivities)
        val list = dbHelper.getRegisteredActNS(id)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }
}