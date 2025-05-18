package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView.Validator
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class GestionDeActividadesSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_de_actividades_socio)

        /*Arrow Back*/
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Inscribir Actividad*/
        val btnRegisterAct: Button =findViewById(R.id.btnRegisterAct)
        btnRegisterAct.setOnClickListener {
            val intentar = Intent(this, InscribirActividadActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Consultar Actividad*/
        val btnConsult: Button =findViewById(R.id.btnConsult)
        btnConsult.setOnClickListener {
            val intentar = Intent (this, ConsulActivityActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Desinscribir Actividad*/
        val btnbtnUnsubscribeActiv: Button =findViewById(R.id.btnUnsubscribeActiv)
        btnbtnUnsubscribeActiv.setOnClickListener {
            val intentar = Intent (this, DesinscribirActividadActivity::class.java)
            startActivity(intentar)
        }
    }
}