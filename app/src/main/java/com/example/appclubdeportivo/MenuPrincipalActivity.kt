package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
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
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
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