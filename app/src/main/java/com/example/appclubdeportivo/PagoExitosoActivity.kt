package com.example.appclubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class PagoExitosoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago_exitoso)

        val nombre = intent.getStringExtra("nombre") ?: "N/A"
        val dni = intent.getIntExtra("dni", 0)
        val monto = intent.getDoubleExtra("monto", 0.0)
        val cuotas = intent.getIntExtra("cuotas", 1)
        val fecha = intent.getStringExtra("fecha") ?: "N/A"

        /* Arrow Back*/
        val btnBack: ImageButton =findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Recibo de pago
        findViewById<TextView>(R.id.nameTextView).text = "Nombre: $nombre"
        findViewById<TextView>(R.id.dniTextView).text = "DNI: $dni"
        findViewById<TextView>(R.id.montoTextView).text =
            if (cuotas > 1) "Monto total: $monto ($cuotas cuotas de ${"%.2f".format(monto / cuotas)})"
            else "Monto: $monto"
        findViewById<TextView>(R.id.fechaTextView).text = "Fecha: $fecha"
        findViewById<TextView>(R.id.idComprobanteTextView).text = "Comprobante N°: ${System.currentTimeMillis()}"

        /* Boton Cerrar */
        val btnClose: Button =findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }


    }
}