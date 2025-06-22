package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class InscribirActividadActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscribir_actividad)

        dbHelper = ClubDBHelper(this)

        val id = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""

        /* Arrow Back*/
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Datos del socio
        findViewById<TextView>(R.id.nameTextView).text = "Socio ID: $id - Nombres: $nombre $apellido"

        // Spinner lista actividades
        val spinner = findViewById<Spinner>(R.id.activitiesSpinner)

        val listActividades = dbHelper.getActivitiesNonSuscribeS(id).toMutableList()
        listActividades.add(0, Actividad(0, "Seleccionar", 0.0))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listActividades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)

        // Boton inscribir actividad
        val btnSuscribe = findViewById<Button>(R.id.btnSuscribe)
        btnSuscribe.setOnClickListener {
            val actSelect = spinner.selectedItem as Actividad
            val idActividad = actSelect.id
            val nombreAct = actSelect.nombre

            if (actSelect.id == 0) {
                Toast.makeText(this, "Debe seleccionar una actividad para desinscribirse", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirmar Inscripción")
                .setMessage("Desea inscribirse en la actividad $nombreAct")
                .setPositiveButton("Si") { _, _, ->
                    dbHelper.activitiesSuscribeS(id, idActividad)
                    Toast.makeText(this, "Actividad Inscrita", Toast.LENGTH_SHORT).show()

                    val nuevasActividades = dbHelper.getActivitiesNonSuscribeS(id).toMutableList()
                    nuevasActividades.add(0, Actividad(0, "Seleccionar", 0.0))
                    val nuevoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nuevasActividades)
                    nuevoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = nuevoAdapter
                    spinner.setSelection(0)
                }
                .setNegativeButton("No", null)
                .show()
        }

        /*Cancelar*/
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }

}
