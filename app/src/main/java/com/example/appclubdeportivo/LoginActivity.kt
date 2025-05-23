package com.example.appclubdeportivo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.TextView

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

        /* Olvide mi contraseña*/
        val forgotPasswordText: TextView =findViewById(R.id.forgotPasswordText)
        forgotPasswordText.setOnClickListener{
            val intentar = Intent(this, RecuperarContrasenaActivity::class.java)
            startActivity(intentar)
        }
    }
}