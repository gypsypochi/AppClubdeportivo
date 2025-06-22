package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
    fun insertMember() {
        val dniStr = findViewById<EditText>(R.id.dniEditText).text.toString()
        val telefonoStr = findViewById<EditText>(R.id.tlfEditText).text.toString()
        val alturaStr = findViewById<EditText>(R.id.heightEditTex).text.toString()

        // Validaciones de campos numéricos
        if (dniStr.isBlank() || telefonoStr.isBlank() || alturaStr.isBlank()) {
            Toast.makeText(this, "DNI, teléfono y altura no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        val dni = dniStr.toIntOrNull()
        val telefono = telefonoStr.toIntOrNull()
        val altura = alturaStr.toIntOrNull()

        if (dni == null || telefono == null || altura == null) {
            Toast.makeText(this, "DNI, teléfono y altura deben ser números válidos", Toast.LENGTH_SHORT).show()
            return
        }

        val nombre = findViewById<EditText>(R.id.nameEditText).text.toString().trim()
        val apellido = findViewById<EditText>(R.id.lastNameEditText).text.toString().trim()
        val nacionalidad = findViewById<EditText>(R.id.nacionalityEditText).text.toString().trim()
        val calle = findViewById<EditText>(R.id.addressEditText).text.toString().trim()
        val barrio = findViewById<EditText>(R.id.districtEditText).text.toString().trim()
        val localidad = findViewById<EditText>(R.id.localityEditText).text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || nacionalidad.isEmpty() ||
            calle.isEmpty() || barrio.isEmpty() || localidad.isEmpty()) {
            Toast.makeText(this, "Por favor completá todos los campos de texto", Toast.LENGTH_SHORT).show()
            return
        }

        val address = Direccion(
            calle = calle,
            altura = altura,
            barrio = barrio,
            localidad = localidad
        )

        val socio = Socio(
            name = nombre,
            lastName = apellido,
            dni = dni,
            nacionality = nacionalidad,
            phoneNum = telefono,
            address = address
        )

        if (dbHelper.isMemberAvailable(socio.dni)) {
            dbHelper.registerPersona("socio", persona = socio, address = address)
            Toast.makeText(this, "Socio Registrado", Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(this, "Error, el DNI ingresado ya existe en nuestro sistema", Toast.LENGTH_SHORT).show()
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