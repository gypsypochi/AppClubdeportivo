package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DesinscribirActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_desinscribir_actividad)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Desinscribir Actividad */
        val btnUnsubscribe: Button =findViewById(R.id.btnUnsubscribe)
        btnUnsubscribe.setOnClickListener {

        }

        /* Boton Cancelar */
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }
}