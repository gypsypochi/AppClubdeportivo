package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class BajaSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    private var idEncontrado: Int = 0
    private var nombres: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_baja_socio)

        dbHelper = ClubDBHelper(this)

        // Ingresar DNI
        val dni = findViewById<EditText>(R.id.dniEditText)


        /*Arrow Back*/
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

        /* Boton Cancelar*/
        val btnCancel: Button = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        /* Boton de Desinscribir*/
        val btnUnsubscribe: Button =findViewById(R.id.btnUnsubscribe)
        btnUnsubscribe.setOnClickListener {
            val dniInput = dni.text.toString()
            if (dniInput.isNotEmpty()) {
                val dniInt = dniInput.toIntOrNull()
                if (dniInt == null) {
                    Toast.makeText(this, "DNI inválido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val socio = dbHelper.getSocio(dniInt)
                if (socio != null) {
                    val (idSocio, nombre, apellido) = socio
                    idEncontrado = idSocio
                    nombres = "$nombre $apellido"
                    AlertDialog.Builder(this)
                        .setTitle("Eliminar Socio")
                        .setMessage("¿Quieres eliminar al socio: $idEncontrado $nombres?")
                        .setPositiveButton("Si") { _, _ ->
                            dbHelper.deleteMember(idEncontrado)
                            dni.text.clear()
                            Toast.makeText(this, "Baja Exitosa", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {
                    Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Debe ingresar un DNI", Toast.LENGTH_SHORT).show()
            }
        }
    }
}