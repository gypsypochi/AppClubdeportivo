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

class DesinscribirActivadNoSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_desinscribir_activad_no_socio)

        dbHelper = ClubDBHelper(this)

        val id = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Datos del socio
        findViewById<TextView>(R.id.nameTextView).text = "No Socio ID: $id - Nombres: $nombre $apellido"

        // Spinner de actividades inscritas
        val spinner = findViewById<Spinner>(R.id.activitiesSpinner)
        cargarSpinner(spinner, id)

        /* Boton Desinscribir Actividad */
        val btnUnsubscribe: Button =findViewById(R.id.btnUnsubscribe)
        btnUnsubscribe.setOnClickListener {
            val actSelect = spinner.selectedItem as Actividad
            val idActividad = actSelect.id
            val nombreAct = actSelect.nombre

            if (actSelect.id == 0) {
                Toast.makeText(this, "Debe seleccionar una actividad para desinscribirse", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Desinscribir")
                .setMessage("Confirma que quiere eliminar la actividad $nombreAct")
                .setPositiveButton("Si") { _, _ ->
                    dbHelper.unsuscribeActNS(id, idActividad)
                    Toast.makeText(this, "Actividad Desinscrita", Toast.LENGTH_SHORT).show()
                    cargarSpinner(spinner, id)
                }
                .setNegativeButton("No", null)
                .show()
        }

        /* Boton Cancelar */
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Cargar spinner
    private fun cargarSpinner(spinner: Spinner, id: Int) {
        val listActividades = dbHelper.getRegisteredActNS(id).toMutableList()
        listActividades.add(0, Actividad(0, "Seleccionar", 0.0))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listActividades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
    }
}