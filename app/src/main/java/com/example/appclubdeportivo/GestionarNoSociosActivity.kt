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

class GestionarNoSociosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestionar_no_socios)

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

        /* Boton Alta No Socio */
        val btnRegisterNonMember: Button =findViewById(R.id.btnRegisterNonMember)
        btnRegisterNonMember.setOnClickListener {
            val intentar = Intent(this, RegistrarNoSocioActivity::class.java)
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
            val intentar = Intent(this, GestionDeActividadesNoSocioActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Baja No Socio */
        val btnDropNonPartner: Button =findViewById(R.id.btnDropNonPartner)
        btnDropNonPartner.setOnClickListener {
            val intentar = Intent(this, BajaNoSocioActivity::class.java)
            startActivity(intentar)
        }

        /*Boton Listado De No Socios*/
        val btnListNonMembers: Button =findViewById(R.id.btnListNonMembers)
        btnListNonMembers.setOnClickListener {
            val intentar = Intent(this, ListadoNoSociosActivity::class.java)
            startActivity(intentar)
        }
    }
}