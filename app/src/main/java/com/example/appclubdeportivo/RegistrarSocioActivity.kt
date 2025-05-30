package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistrarSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_socio)

        dbHelper = ClubDBHelper(this)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Inscribir */
        val btnRegister: Button =findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            insertMember()
        }

        /* Boton Cancelar */
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Funcion alta socio
    fun insertMember(){
        val name = findViewById<EditText>(R.id.nameEditText).text.toString()
        val lastName = findViewById<EditText>(R.id.lastNameEditText).text.toString()
        val dni = findViewById<EditText>(R.id.dniEditText).text.toString().toInt()
        val nacionality = findViewById<EditText>(R.id.nacionalityEditText).text.toString()
        val tlf = findViewById<EditText>(R.id.tlfEditText).text.toString().toInt()

        val direccion = Direccion (
            calle = findViewById<EditText>(R.id.addressEditText).text.toString(),
            altura = findViewById<EditText>(R.id.heightEditTex).text.toString().toInt(),
            barrio = findViewById<EditText>(R.id.districtEditText).text.toString(),
            localidad = findViewById<EditText>(R.id.localityEditText).text.toString())

        if (dbHelper.isMemberAvailable(dni)) {
            dbHelper.registerMember(name, lastName, dni, nacionality, tlf, direccion)
            Toast.makeText(this, "Socio Registrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "El dni pertenece a un socio registrado", Toast.LENGTH_SHORT).show()
        }
    }
}