package com.example.appclubdeportivo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        dbHelper = ClubDBHelper(this)

        val user = findViewById<EditText>(R.id.usernameEditText)
        val pass = findViewById<EditText>(R.id.passwordEditText)

        /* Boton Logueo*/
        val btnLogin: Button =findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val u = user.text.toString().trim()
            val p = pass.text.toString().trim()
            if (dbHelper.login(u, p)) {
                Toast.makeText(this, "Bienvenido $u", Toast.LENGTH_SHORT).show()
                val intentar = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intentar)
            } else {
                Toast.makeText(this, "Acceso Denegado", Toast.LENGTH_SHORT).show()
            }
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