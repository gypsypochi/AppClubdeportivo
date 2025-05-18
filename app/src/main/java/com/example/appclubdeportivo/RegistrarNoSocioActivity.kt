package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistrarNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_no_socio)

        /*Arrow Back*/
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Cancelar */
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        /* Boton Inscribir */
        val btnRegister: Button =findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            Toast.makeText(this, "Inscripción Exitosa", Toast.LENGTH_SHORT).show()
        }
    }
}