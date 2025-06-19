package com.example.appclubdeportivo

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistrarPagosActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper
    var monto: Double = 0.0
    private var dniConsultado: Int = 0
    private var nombreSocio: String = ""
    private var cuotasSeleccionadas: Int = 1
    private var fechaPago: String = ""
    private var socioDatos: Triple<Int, String, String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_pagos)

        dbHelper = ClubDBHelper(this)

        val dni = findViewById<EditText>(R.id.documentEditText)
        val btnConsult = findViewById<Button>(R.id.btnConsult)
        val montoTextView = findViewById<TextView>(R.id.montoTextView)
        val rgMetodoPago = findViewById<RadioGroup>(R.id.rgMetodoPago)
        val rgCuotas = findViewById<RadioGroup>(R.id.rgCuotas)

        /* Arrow Back */
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        /* Boton Consultar */

        btnConsult.setOnClickListener {
            val dniInput = dni.text.toString()

            if (dniInput.isNotEmpty()) {
                val dni = dniInput.toInt()

                val socio = dbHelper.getSocio(dni)
                socioDatos = socio
                val idNoSocio = dbHelper.getNoSocio(dni)

                when {
                    socio != null -> {
                        val (idSocio, nombre, apellido) = socio
                        nombreSocio = "$nombre $apellido"
                        dniConsultado = dni
                        Toast.makeText(this, "Es Socio: $nombre $apellido (ID: $idSocio)", Toast.LENGTH_SHORT).show()

                        monto = dbHelper.getQuotMonthly(idSocio) ?: 0.0
                        montoTextView.text = "Total a pagar: $monto"
                    }
                    idNoSocio != null -> {
                        Toast.makeText(this, "Es noSocio. ID: $idNoSocio", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "DNI no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Seleccion efectivo o tarjeta
        rgMetodoPago.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCash -> {
                    rgCuotas.visibility = RadioGroup.GONE
                    montoTextView.text = "Total a pagar: $monto"
                }
                R.id.rbCredit -> {
                    rgCuotas.visibility = RadioGroup.VISIBLE
                    val selectedCuotaId = rgCuotas.checkedRadioButtonId
                    if (selectedCuotaId != -1) {

                    }
                }
            }
        }

        rgCuotas.setOnCheckedChangeListener { _, checkedId ->
            actualizarMontoPorCuotas(checkedId, montoTextView)
        }

        /* Boton Confirmar */
        val btnConfirm: Button = findViewById(R.id.btnConfirm)
        btnConfirm.setOnClickListener {

            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            fechaPago = sdf.format(java.util.Date())

            val idSocio = socioDatos?.first ?: return@setOnClickListener
            dbHelper.monthlyPayment(
                idSocio = idSocio,
                cuotaMensual = monto,
                fechaPago = fechaPago
            )

            val intento = Intent(this, PagoExitosoActivity::class.java).apply {
                putExtra("nombre", nombreSocio)
                putExtra("dni", dniConsultado)
                putExtra("monto", monto)
                putExtra("cuotas", cuotasSeleccionadas)
                putExtra("fecha", fechaPago)
            }

            startActivity(intento)
        }

    }

        fun actualizarMontoPorCuotas(checkedId: Int, montoTextView: TextView) {
            val cuotas = when (checkedId) {
                R.id.rb1Quot -> 1
                R.id.rb3Quot -> 3
                R.id.rb6Quot -> 6
                else -> 1
            }
            val montoPorCuota = monto / cuotas
            val formatter = DecimalFormat("#.00")
            val montoFormateado = formatter.format(monto)
            val cuotaFormateada = formatter.format(montoPorCuota)
            montoTextView.text = "Total: $montoFormateado\n$cuotas cuotas de $cuotaFormateada"
        }
}