package com.example.appclubdeportivo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class RegistrarSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_socio)

        dbHelper = ClubDBHelper(this)

        val fitnessCertificate = findViewById<LinearLayout>(R.id.fitnessCertificate)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Inscribir */
        val btnRegister: Button =findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            fitnessCertificate.visibility = View.VISIBLE
            val btnYes = findViewById<Button>(R.id.btnYes)
            val btnNo = findViewById<Button>(R.id.btnNo)

            btnYes.setOnClickListener{
                insertMember()
                fitnessCertificate.visibility = View.GONE
            }

            btnNo.setOnClickListener{
                Toast.makeText(this, "Es necesario el apto físico para dar el alta", Toast.LENGTH_SHORT).show()
                fitnessCertificate.visibility = View.GONE
            }
        }

        /* Boton Cancelar */
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Funcion alta socio
    fun insertMember(){
        val address = Direccion (
            calle = findViewById<EditText>(R.id.addressEditText).text.toString(),
            altura = findViewById<EditText>(R.id.heightEditTex).text.toString().toInt(),
            barrio = findViewById<EditText>(R.id.districtEditText).text.toString(),
            localidad = findViewById<EditText>(R.id.localityEditText).text.toString())

        val socio = Socio(
            name = findViewById<EditText>(R.id.nameEditText).text.toString(),
            lastName = findViewById<EditText>(R.id.lastNameEditText).text.toString(),
            dni = findViewById<EditText>(R.id.dniEditText).text.toString().toInt(),
            nacionality = findViewById<EditText>(R.id.nacionalityEditText).text.toString(),
            phoneNum = findViewById<EditText>(R.id.tlfEditText).text.toString().toInt(),
            address = address)

        if (dbHelper.isMemberAvailable(socio.dni)) {
            dbHelper.registerPersona("socio", persona = socio, address = address)
            Toast.makeText(this, "Socio Registrado", Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(this, "Error, el dni ingresado ya existe en nuestro sistema", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para borrar campos del formulario
    private fun clearForm() {
        findViewById<EditText>(R.id.nameEditText).text.clear()
        findViewById<EditText>(R.id.lastNameEditText).text.clear()
        findViewById<EditText>(R.id.dniEditText).text.clear()
        findViewById<EditText>(R.id.nacionalityEditText).text.clear()
        findViewById<EditText>(R.id.tlfEditText).text.clear()
        findViewById<EditText>(R.id.addressEditText).text.clear()
        findViewById<EditText>(R.id.heightEditTex).text.clear()
        findViewById<EditText>(R.id.districtEditText).text.clear()
        findViewById<EditText>(R.id.localityEditText).text.clear()
    }
}