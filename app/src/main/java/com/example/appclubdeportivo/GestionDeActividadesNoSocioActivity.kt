package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class GestionDeActividadesNoSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    private var id: Int = 0
    private var nameNoSocio: String = ""
    private var lastNameNS: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_de_actividades_no_socio)

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
                        val noSocio = dbHelper.getNoSocio(dniInt)

                        if (noSocio != null) {
                            val (idNoSocio, nombre, apellido) = noSocio
                            id = idNoSocio
                            nameNoSocio = nombre
                            lastNameNS = apellido
                        }

                        // Habilitar botones si el socio existe
                        val noSocioExiste = noSocio != null
                        btnRegisterAct.isEnabled = noSocioExiste
                        btnConsult.isEnabled = noSocioExiste
                        btnUnsubscribeActiv.isEnabled = noSocioExiste
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

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Inscribir Actividad */
        btnRegisterAct.setOnClickListener {
            val intentar = Intent(this, InscribirActividadNoSocioActivity::class.java).apply {
                putExtra("id", id)
                putExtra("nombre", nameNoSocio)
                putExtra("apellido", lastNameNS)
            }
            startActivity(intentar)
            dni.text.clear()
        }

        /*Consultar Actividad*/
        btnConsult.setOnClickListener {
            val intentar = Intent(this, ConsulActividadNoSocioActivity::class.java).apply {
                putExtra("id", id)
                putExtra("nombre", nameNoSocio)
                putExtra("apellido", lastNameNS)
            }
            startActivity(intentar)
            dni.text.clear()
        }

        /* Boton Desinscribir Actividad */
        btnUnsubscribeActiv.setOnClickListener {
            val intentar = Intent(this, DesinscribirActivadNoSocioActivity::class.java).apply {
                putExtra("id", id)
                putExtra("nombre", nameNoSocio)
                putExtra("apellido", lastNameNS)
            }
            startActivity(intentar)
            dni.text.clear()
        }
    }
}