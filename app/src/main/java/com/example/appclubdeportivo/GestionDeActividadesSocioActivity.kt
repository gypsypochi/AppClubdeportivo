package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView.Validator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class GestionDeActividadesSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_de_actividades_socio)

        dbHelper = ClubDBHelper(this)

        val btnRegisterAct: Button =findViewById(R.id.btnRegisterAct)
        val btnConsult: Button =findViewById(R.id.btnConsult)
        val btnUnsubscribeActiv: Button =findViewById(R.id.btnUnsubscribeActiv)

        // Desabilitamos los botones hasta que se ingrese un dni valido
        btnRegisterAct.isEnabled = false
        btnConsult.isEnabled = false
        btnUnsubscribeActiv.isEnabled = false

        // DNI
        val dni = findViewById<EditText>(R.id.dniEditText)

        dni.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val dniStr = s.toString()
                if (dniStr.isNotEmpty()) {
                    val dniInt = dniStr.toIntOrNull()
                    if (dniInt != null) {
                        val socio = dbHelper.getSocio(dniInt)
                        val socioExiste = socio != null

                        // Habilitar botones si el socio existe
                        btnRegisterAct.isEnabled = socioExiste
                        btnConsult.isEnabled = socioExiste
                        btnUnsubscribeActiv.isEnabled = socioExiste
                    } else {
                        // Si el texto no es un número válido
                        btnRegisterAct.isEnabled = false
                        btnConsult.isEnabled = false
                        btnUnsubscribeActiv.isEnabled = false
                    }
                } else {
                    // Si el campo está vacío
                    btnRegisterAct.isEnabled = false
                    btnConsult.isEnabled = false
                    btnUnsubscribeActiv.isEnabled = false
                }
            }
        })

        /*Arrow Back*/
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Inscribir Actividad*/
        btnRegisterAct.setOnClickListener {
            val intentar = Intent(this, InscribirActividadActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Consultar Actividad*/
        btnConsult.setOnClickListener {
            val intentar = Intent (this, ConsulActivityActivity::class.java)
            startActivity(intentar)
        }

        /* Boton Desinscribir Actividad*/
        btnUnsubscribeActiv.setOnClickListener {
            val intentar = Intent (this, DesinscribirActividadActivity::class.java)
            startActivity(intentar)
        }
    }
}