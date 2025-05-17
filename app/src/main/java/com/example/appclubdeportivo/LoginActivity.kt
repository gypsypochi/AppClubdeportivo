package com.example.appclubdeportivo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        /* Boton Logueo*/
        val btnLogin: Button =findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val intentar = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Registro */
        val btnSignUp: Button =findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intentar = Intent(this, RegisterActivity::class.java)
            startActivity(intentar)
        }
    }
}