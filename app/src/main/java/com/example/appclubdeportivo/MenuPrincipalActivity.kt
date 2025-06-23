package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
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

        /* Boton Gestionar Socios */
        val btnMembers: Button =findViewById(R.id.btnMembers)
        btnMembers.setOnClickListener{
            val intentar = Intent(this, GestionarSociosActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Gestionar No Socios */
        val btnNonMembers: Button =findViewById(R.id.btnNonMembers)
        btnNonMembers.setOnClickListener{
            val intentar = Intent(this, GestionarNoSociosActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Mostrar Deudores */
        val btnShowDebtors: Button =findViewById(R.id.btnShowDebtors)
        btnShowDebtors.setOnClickListener{
            val intentar = Intent(this, ListadoDeudoresActivity::class.java)
            startActivity(intentar)
        }
    }
}