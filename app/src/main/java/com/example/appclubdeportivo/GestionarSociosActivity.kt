package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class GestionarSociosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestionar_socios)

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

        /* Boton Alta Socio */
        val btnRegisterMember: Button =findViewById(R.id.btnRegisterMember)
        btnRegisterMember.setOnClickListener {
            val intentar = Intent(this, RegistrarSocioActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Pagar Cuota */
        val btnPayFee: Button =findViewById(R.id.btnPayFee)
        btnPayFee.setOnClickListener {
            val intentar = Intent(this, RegistrarPagosActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Actividades */
        val btnActivities: Button =findViewById(R.id.btnActivities)
        btnActivities.setOnClickListener {
            val intentar = Intent(this, GestionDeActividadesSocioActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Baja Socio */
        val btnDropPartner: Button =findViewById(R.id.btnDropPartner)
        btnDropPartner.setOnClickListener {
            val intentar = Intent(this, BajaSocioActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Lista Socios */
        val btnListMembers: Button =findViewById(R.id.btnListMembers)
        btnListMembers.setOnClickListener {
            val intentar = Intent(this, ListadoSociosActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Generar Carnet */
        val btnGenerateCard: Button =findViewById(R.id.btnGenerateCard)
        btnGenerateCard.setOnClickListener {
            val intentar = Intent(this, CarnetActivity::class.java)
            startActivity(intentar)
        }
    }
}