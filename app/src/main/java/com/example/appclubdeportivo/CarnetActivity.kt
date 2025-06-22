package com.example.appclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class CarnetActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    private var id: Int = 0
    private var idText: String = ""
    private var nomSocio: String = ""
    private var dniConsult: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet)

        dbHelper = ClubDBHelper(this)

        val dni = findViewById<EditText>(R.id.documentTextText)

        val carnet = findViewById<CardView>(R.id.cardView)
        carnet.visibility = View.GONE

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

        // Boton Consultar
        val btnConsult = findViewById<Button>(R.id.btnConsultar)
        btnConsult.setOnClickListener {
            val dniInput = dni.text.toString()
            if (dniInput.isNotEmpty()) {
                val dniNum = dniInput.toInt()

                val socio = dbHelper.getSocio(dniNum)
                if (socio != null) {
                    val (idSocio, nombre, apellido) = socio
                    id = idSocio
                    idText = "ID Socio: $id"
                    nomSocio = "Nombres: $nombre $apellido"
                    dniConsult = "Documento: $dniNum"
                    carnet.visibility = View.VISIBLE
                    findViewById<TextView>(R.id.idMemberTextView).text = idText
                    findViewById<TextView>(R.id.namePartnerTextView).text = nomSocio
                    findViewById<TextView>(R.id.dniTextView).text = dniConsult
                    dni.text.clear()
                } else {
                    Toast.makeText(this, "El DNI no pertenece a un socio", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /*Boton Cancelar*/
        val btnCancel: Button =findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }
}