package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

        // Datos del no socio
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