package com.example.appclubdeportivo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton

class ListadoDeudoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_deudores)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }

        /* Boton Cerrar */
        val btnClose: Button =findViewById(R.id.btnClose)
        btnClose.setOnClickListener{
            finish()
        }
    }
}