package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        dbHelper = ClubDBHelper(this)

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
            insertUser()
        }
    }

    private fun insertUser () {
        val userNameEditText = findViewById<EditText>(R.id.nameRegisterEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordRegisterEditText)

        val userName = userNameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (userName.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.registerUser(userName, password)
            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
            userNameEditText.text.clear()
            passwordEditText.text.clear()
        }
    }
}