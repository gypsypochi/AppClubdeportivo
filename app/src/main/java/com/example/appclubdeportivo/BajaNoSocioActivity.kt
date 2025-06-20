package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class BajaNoSocioActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    private var idEncontrado: Int = 0
    private var nombres: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_baja_no_socio)

        dbHelper = ClubDBHelper(this)

        // Ingresar DNI
        val dni = findViewById<EditText>(R.id.dniEditText)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Dar De Baja Del Sistema */
        val btnUnsubscribe: Button =findViewById(R.id.btnUnsubscribe)
        btnUnsubscribe.setOnClickListener {
            val dniInput = dni.text.toString()
            if (dniInput.isNotEmpty()) {
                val dniInt = dniInput.toIntOrNull()
                if (dniInt == null) {
                    Toast.makeText(this, "DNI inválido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val noSocio = dbHelper.getNoSocio(dniInt)
                if (noSocio != null) {
                    val (idNoSocio, nombre, apellido) = noSocio
                    idEncontrado = idNoSocio
                    nombres = "$nombre $apellido"
                    AlertDialog.Builder(this)
                        .setTitle("Eliminar Socio")
                        .setMessage("¿Quieres eliminar al no socio: $idEncontrado $nombres?")
                        .setPositiveButton("Si") { _, _ ->
                            dbHelper.deleteNonMember(idEncontrado)
                            dni.text.clear()
                            Toast.makeText(this, "Baja Exitosa", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {
                    Toast.makeText(this, "No Socio no encontrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Debe ingresar un DNI", Toast.LENGTH_SHORT).show()
            }
        }

        /* Boton cancelar */
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }
}