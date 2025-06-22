package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView.Validator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GestionDeActividadesSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    private var id: Int = 0
    private var nameSocio: String = ""
    private var lastNameS: String = ""

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

                        if (socio != null) {
                            val (idSocio, nombre, apellido) = socio
                            id = idSocio
                            nameSocio = nombre
                            lastNameS = apellido
                        }

                        // Habilitar botones si el socio existe
                        val socioExiste = socio != null
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

        // Menu Desplegable
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener { view ->

            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_lateral, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navMenuPrincipal -> {
                        val intentar = Intent(this, MenuPrincipalActivity::class.java)
                        startActivity(intentar)
                        true
                    }
                    R.id.navSocios -> {
                        val intentar = Intent(this, GestionarSociosActivity::class.java)
                        startActivity(intentar)
                        true
                    }
                    R.id.navNoSocios -> {
                        val intentar = Intent(this, GestionarNoSociosActivity::class.java)
                        startActivity(intentar)
                        true
                    }
                    R.id.navLogOut -> {
                        AlertDialog.Builder(this)
                            .setTitle("Cerrar Sesión")
                            .setMessage("Quieres finalizar la sesión")
                            .setPositiveButton("Si") {_, _, ->
                                Toast.makeText(this, "Sesión Finalizada", Toast.LENGTH_SHORT).show()
                                val intentar = Intent(this, LoginActivity::class.java)
                                startActivity(intentar)
                            }
                            .setNegativeButton("No", null)
                            .show()
                        true
                    }
                    R.id.navCloseMenu -> {
                        closeOptionsMenu()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        /* Boton Inscribir Actividad */
        btnRegisterAct.setOnClickListener {
            val intentar = Intent(this, InscribirActividadActivity::class.java).apply {
                putExtra("id", id)
                putExtra("nombre", nameSocio)
                putExtra("apellido", lastNameS)
            }
            startActivity(intentar)
            dni.text.clear()
        }

        /* Boton Consultar Actividad*/
        btnConsult.setOnClickListener {
            val intentar = Intent (this, ConsulActivityActivity::class.java).apply {
                putExtra("id", id)
                putExtra("nombre", nameSocio)
                putExtra("apellido", lastNameS)
            }
            startActivity(intentar)
            dni.text.clear()
        }

        /* Boton Desinscribir Actividad*/
        btnUnsubscribeActiv.setOnClickListener {
            val intentar = Intent (this, DesinscribirActividadActivity::class.java).apply {
                putExtra("id", id)
                putExtra("nombre", nameSocio)
                putExtra("apellido", lastNameS)
            }
            startActivity(intentar)
            dni.text.clear()
        }
    }
}