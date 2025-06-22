package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DesinscribirActividadActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_desinscribir_actividad)

        dbHelper = ClubDBHelper(this)

        val id = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Menu Desplegable
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener { view ->

            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_lateral, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navMenuPrincipal -> {
                        val intentar = Intent(this, MenuPrincipalActivity::class.java)
                        startActivity(intentar)
                        true
                    }
                    R.id.navSocios -> {
                        val intentar = Intent(this, GestionarSociosActivity::class.java)
                        startActivity(intentar)
                        true
                    }
                    R.id.navNoSocios -> {
                        val intentar = Intent(this, GestionarNoSociosActivity::class.java)
                        startActivity(intentar)
                        true
                    }
                    R.id.navLogOut -> {
                        AlertDialog.Builder(this)
                            .setTitle("Cerrar Sesión")
                            .setMessage("Quieres finalizar la sesión")
                            .setPositiveButton("Si") {_, _, ->
                                Toast.makeText(this, "Sesión Finalizada", Toast.LENGTH_SHORT).show()
                                val intentar = Intent(this, LoginActivity::class.java)
                                startActivity(intentar)
                            }
                            .setNegativeButton("No", null)
                            .show()
                        true
                    }
                    R.id.navCloseMenu -> {
                        closeOptionsMenu()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        // Datos del socio
        findViewById<TextView>(R.id.nameTextView).text = "Socio ID: $id - Nombres: $nombre $apellido"

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
                    dbHelper.unsuscribeAct(id, idActividad)
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
        val listActividades = dbHelper.getRegisteredAct(id).toMutableList()
        listActividades.add(0, Actividad(0, "Seleccionar", 0.0))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listActividades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
    }
}