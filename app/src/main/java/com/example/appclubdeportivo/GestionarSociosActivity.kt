package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
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