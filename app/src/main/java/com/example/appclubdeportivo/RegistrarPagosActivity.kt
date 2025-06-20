package com.example.appclubdeportivo

import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
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
import java.util.Date
import java.util.Locale

class RegistrarPagosActivity : AppCompatActivity() {

    lateinit var dbHelper: ClubDBHelper

    var monto: Double = 0.0
    private var dniConsultado: Int = 0
    private var nombreSocio: String = ""
    private var cuotasSeleccionadas: Int = 1
    private var fechaPago: String = ""
    private var socioDatos: Triple<Int, String, String>? = null
    private var esNoSocio: Boolean = false


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
        val btnConfirm: Button = findViewById(R.id.btnConfirm)
        btnConfirm.isEnabled = false

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
                val noSocio = dbHelper.getNoSocio(dni)

                when {
                    socio != null -> {
                        val (idSocio, nombre, apellido) = socio
                        nombreSocio = "$nombre $apellido"
                        dniConsultado = dni
                        Toast.makeText(this, "Es Socio: $nombre $apellido (ID: $idSocio)", Toast.LENGTH_SHORT).show()
                        btnConfirm.isEnabled = true

                        monto = dbHelper.getQuotMonthly(idSocio) ?: 0.0
                        montoTextView.text = "Total a pagar: $monto"
                        esNoSocio = false
                    }
                    noSocio != null -> {
                        val (idNoSocio, nombre, apellido) = noSocio
                        nombreSocio = "$nombre, $apellido"
                        dniConsultado = dni
                        monto = dbHelper.getDailyFee(dni)
                        montoTextView.text = "Total a pagar: $monto"
                        Toast.makeText(this, "Es noSocio. ID: $idNoSocio", Toast.LENGTH_SHORT).show()
                        btnConfirm.isEnabled = true

                        esNoSocio = true
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
                    rgCuotas.clearCheck()
                    rgCuotas.visibility = RadioGroup.GONE
                    montoTextView.text = "Total a pagar: $monto"
                }
                R.id.rbCredit -> {
                    if (esNoSocio) {
                        rgCuotas.clearCheck()
                        rgCuotas.visibility = RadioGroup.GONE
                        cuotasSeleccionadas = 1 // No se permiten cuotas
                        montoTextView.text = "Total a pagar: $monto"
                    } else {
                        rgCuotas.visibility = RadioGroup.VISIBLE
                    }
                }
            }
        }

        rgCuotas.setOnCheckedChangeListener { _, checkedId ->
            actualizarMontoPorCuotas(checkedId, montoTextView)
        }

        /* Boton Confirmar */
        btnConfirm.setOnClickListener {

            val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            fechaPago = sdf.format(Date())

            // Si el monto es 0.0 no permite pagar
            if (monto <= 0.0) {
                Toast.makeText(this, "El monto no puede ser cero", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Consultamos si el socio o noSocio que quiere pagar tiene la cuota vencida
            val venceCuota = dbHelper.getVenceCuota(dniConsultado)

            val puedePagar = venceCuota.isNullOrEmpty() || run {
                val formatoVencimiento = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaVencimiento = formatoVencimiento.parse(venceCuota)
                val fechaActual = formatoVencimiento.parse(
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
                fechaVencimiento != null && (
                        fechaActual.after(fechaVencimiento) || fechaActual == fechaVencimiento
                        )
            }

            if (!puedePagar) {
                Toast.makeText(this, "La cuota no esta vencida", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (esNoSocio) {
                dbHelper.dailyPayment(dniConsultado, fechaPago)
            } else {
                val idSocio = socioDatos?.first ?: return@setOnClickListener
                dbHelper.monthlyPayment(idSocio, monto, fechaPago)
            }

            // Intent para pasar a la activity de pago exitoso y llevar los datos para el recibo
            val intento = Intent(this, PagoExitosoActivity::class.java).apply {
                putExtra("nombre", nombreSocio)
                putExtra("dni", dniConsultado)
                putExtra("monto", monto)
                putExtra("cuotas", cuotasSeleccionadas)
                putExtra("fecha", fechaPago)
            }

            startActivity(intento)
            finish()
        }

        // Boton Cancelar
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
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