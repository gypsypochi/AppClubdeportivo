package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Ingresar */
        val btnSignIn: Button =findViewById(R.id.btnSignIn)
        btnSignIn.setOnClickListener {
            val intentar = Intent(this, LoginActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Confirmar */
        val btnConfirmRegister: Button =findViewById(R.id.btnConfirmRegister)
        btnConfirmRegister.setOnClickListener {
            val intentar = Intent(this, LoginActivity::class.java)
            startActivity(intentar)
        }
    }
}