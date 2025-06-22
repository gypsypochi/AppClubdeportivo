package com.example.appclubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.util.Calendar
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClubDBHelper (context: Context): SQLiteOpenHelper(context, "ClubDB", null, 5) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {

    // Crear las tablas
        db.execSQL("""
            CREATE TABLE direccion (
            idDireccion INTEGER PRIMARY KEY AUTOINCREMENT,
            calle TEXT,
            altura INTEGER,
            barrio TEXT,
            localidad TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE socio (
            idSocio INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            apellido TEXT,
            dni INTEGER UNIQUE,
            nacionalidad TEXT,
            telefono INTEGER,
            idDireccion INTEGER,
            venceCuota TEXT,
            FOREIGN KEY (idDireccion) REFERENCES direccion(idDireccion) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE noSocio (
            idNoSocio INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            apellido TEXT,
            dni INTEGER UNIQUE,
            nacionalidad TEXT,
            telefono INTEGER,
            idDireccion INTEGER,
            venceCuota TEXT,
            FOREIGN KEY (idDireccion) REFERENCES direccion(idDireccion) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE actividad (
            idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            cuotaDiaria REAL,
            maxInscritos INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE socioActividad (
            idSocioActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            idSocio INTEGER,
            idActividad INTEGER,
            FOREIGN KEY (idSocio) REFERENCES socio(idSocio) ON DELETE CASCADE,
            FOREIGN KEY (idActividad) REFERENCES actividad(idActividad)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE noSocioActividad (
            idNoSocioActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            idNoSocio INTEGER,
            idActividad INTEGER,
            FOREIGN KEY (idNoSocio) REFERENCES noSocio(idNoSocio) ON DELETE CASCADE,
            FOREIGN KEY (idActividad) REFERENCES actividad(idActividad)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE pagoMensual (
            idPago INTEGER PRIMARY KEY AUTOINCREMENT,
            idSocio INTEGER,
            cuotaMensual REAL DEFAULT 25000.00,
            fechaPago TEXT,
            FOREIGN KEY (idSocio) REFERENCES socio(idSocio) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE user (
            idUser INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            password TEXT
            )
        """.trimIndent())

        // Usuarios para ingresar al sistema
        db.execSQL("INSERT INTO user (userName, password) VALUES ('Veronica', '12345')")
        db.execSQL("INSERT INTO user (userName, password) VALUES ('Micaela', '12123')")
        db.execSQL("INSERT INTO user (userName, password) VALUES ('Pedro', '12321')")
        db.execSQL("INSERT INTO user (userName, password) VALUES ('Kevin', '12345')")

        // Actividades disponibles
        db.execSQL("""
            INSERT INTO actividad (nombre, cuotaDiaria, maxInscritos) VALUES
            ("Futbol", 1500.00, 25),
            ("Baloncesto", 1500.00, 25),
            ("Natación", 1500.00, 25),
            ("Gimnasia", 1500.00, 25),
            ("Atletismo", 1500.00, 25),
            ("Escalada", 1500.00, 25),
            ("Tenis", 1500.00, 25),
            ("Tenis de Mesa", 1500.00, 25)
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS noSocioActividad")
        db.execSQL("DROP TABLE IF EXISTS socioActividad")
        db.execSQL("DROP TABLE IF EXISTS actividad")
        db.execSQL("DROP TABLE IF EXISTS noSocio")
        db.execSQL("DROP TABLE IF EXISTS socio")
        db.execSQL("DROP TABLE IF EXISTS direccion")
        db.execSQL("DROP TABLE IF EXISTS pagoMensual")

        onCreate(db)
    }

    // Funcion Login
    fun login(userName: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM user WHERE userName=? AND password=?",
            arrayOf(userName, password)
        )
        val exitoso = cursor.count > 0
        cursor.close()
        return exitoso
    }

    // Funcion buscar socio/noSocio por dni
    fun isMemberAvailable(dni: Int): Boolean {
        val db = readableDatabase
        val query = """
        SELECT dni FROM socio WHERE dni = ? 
        UNION 
        SELECT dni FROM noSocio WHERE dni = ?
        """
        val cursor = db.rawQuery(query, arrayOf(dni.toString(), dni.toString()))
        val exists = cursor.count == 0  // True si el DNI no existe
        cursor.close()
        return exists
    }

    // Funcion registrar socio/noSocio
    fun registerPersona(tableName: String, persona: Persona, address: Direccion): Boolean {
        val db = writableDatabase

        // Se registra la direccion
        val valuesDireccion = ContentValues().apply {
            put("calle", address.calle)
            put("altura", address.altura)
            put("barrio", address.barrio)
            put("localidad", address.localidad)
        }
        val idDireccion = db.insert("direccion", null, valuesDireccion)
        if (idDireccion == -1L) return false

        //Se registra al Socio
        val values = ContentValues().apply {
            put("nombre", persona.name)
            put("apellido", persona.lastName)
            put("dni", persona.dni)
            put("nacionalidad", persona.nacionality)
            put("telefono", persona.phoneNum)
            put("idDireccion", idDireccion.toInt())
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaActual = sdf.format(Date())
            put("venceCuota", fechaActual)
        }

        // Si es socio se registra el monto del pago mensual
        val idPersona = db.insert(tableName, null, values)
        if (idPersona == -1L) return false

        if (tableName == "socio") {
            val pagoValues = ContentValues().apply {
                put("idSocio", idPersona.toInt())
            }

            val idPago = db.insert("pagoMensual", null, pagoValues)
            if (idPago == -1L) return false
        }

        return true
    }

    // Funcion lista de socios
    fun listSocios():List<String>{
        val socios = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT idSocio, nombre, apellido, dni, telefono, venceCuota FROM socio", null)
        if (cursor.moveToFirst()) {
            do {
                val idSocio = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val dni = cursor.getString(3)
                val telefono = cursor.getString(4)
                val venceCuota = cursor.getString(5)

                val socioInfo = "ID: $idSocio - $nombre $apellido - DNI: $dni - Tel: $telefono - Vence: $venceCuota"
                socios.add(socioInfo)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return socios
    }

    // Funcion lista de no socios
    fun listNoSocios():List<String>{
        val noSocios = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT idNoSocio, nombre, apellido, dni, telefono, venceCuota FROM noSocio", null)
        if (cursor.moveToFirst()) {
            do {
                val idNoSocio = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val apellido = cursor.getString(2)
                val dni = cursor.getString(3)
                val telefono = cursor.getString(4)
                val venceCuota = cursor.getString(5)

                val socioInfo = "ID: $idNoSocio - $nombre $apellido - DNI: $dni - Tel: $telefono - Vence: $venceCuota"
                noSocios.add(socioInfo)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return noSocios
    }

    // Funcion lista de deudores
    fun getDeudores():List<String>{
        val deudores = mutableListOf<String>()
        val db = readableDatabase

        val vencimiento = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = vencimiento.format(Date())

        val cursor = db.rawQuery("SELECT nombre, apellido, dni, telefono, venceCuota FROM socio WHERE venceCuota = ? ", arrayOf(fechaActual))
        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(0)
                val apellido = cursor.getString(1)
                val dni = cursor.getString(2)
                val telefono = cursor.getString(3)
                val venceCuota = cursor.getString(4)

                val socioInfo = "Nombre: $nombre $apellido - DNI: $dni - Tel: $telefono - Vence: $venceCuota"
                deudores.add(socioInfo)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return deudores
    }

    // Buscar socio por dni
    fun getSocio(dni: Int): Triple<Int, String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT idSocio, nombre, apellido FROM socio WHERE dni = ?", arrayOf(dni.toString()))

        var resultado: Triple<Int, String, String>? = null
        if (cursor.moveToFirst()) {
            val idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("idSocio"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            resultado = Triple(idSocio, nombre, apellido)
        }

        cursor.close()
        return resultado
    }

    // Buscar no socio por dni
    fun getNoSocio(dni: Int): Triple<Int, String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT idNoSocio, nombre, apellido FROM noSocio WHERE dni = ?", arrayOf(dni.toString()))

        var resultado: Triple<Int, String, String>? = null
        if (cursor.moveToFirst()) {
            val idNoSocio = cursor.getInt(cursor.getColumnIndexOrThrow("idNoSocio"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            resultado = Triple(idNoSocio, nombre, apellido)
        }

        cursor.close()
        return resultado
    }

    // Pago cuota mensual
    fun monthlyPayment(idSocio: Int, cuotaMensual: Double, fechaPago: String) {
        val db = writableDatabase

        // Insertar pago
        val values = ContentValues().apply {
            put("idSocio", idSocio)
            put("cuotaMensual", cuotaMensual)
            put("fechaPago", fechaPago)
        }
        val resultado = db.insert("pagoMensual", null, values)
        if (resultado == -1L) {
            throw Exception("Error al registrar el pago mensual")
        }

        // Actualizar venceCuota
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(fechaPago)!!
        calendar.add(Calendar.MONTH, 1)
        val nuevaFechaVencimiento = formatter.format(calendar.time)

        val updateStmt = db.compileStatement("""
        UPDATE socio SET venceCuota = ? WHERE idSocio = ?
    """.trimIndent())
        updateStmt.bindString(1, nuevaFechaVencimiento)
        updateStmt.bindLong(2, idSocio.toLong())
        updateStmt.executeUpdateDelete()

        db.close()
    }

    // Pago cuota diaria
    fun dailyPayment (dni: Int) {
        val db = writableDatabase

        val cursor = db.rawQuery("SELECT idNoSocio FROM noSocio WHERE dni = ?", arrayOf(dni.toString()))
        if (cursor.moveToFirst()) {
            val idNoSocio = cursor.getInt(cursor.getColumnIndexOrThrow("idNoSocio"))

            // Actualizar venceCuota
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val nuevaFechaVencimiento = formatter.format(calendar.time)

            val updateStmt = db.compileStatement(
                """
        UPDATE noSocio SET venceCuota = ? WHERE idNoSocio = ?
    """.trimIndent()
            )
            updateStmt.bindString(1, nuevaFechaVencimiento)
            updateStmt.bindLong(2, idNoSocio.toLong())
            updateStmt.executeUpdateDelete()
        }

        cursor.close()
        db.close()
    }

    // Buscar cuotaMensual
    fun getQuotMonthly(idSocio: Int): Double? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT cuotaMensual FROM pagoMensual Where idSocio = ?", arrayOf(idSocio.toString()))

        var monto: Double? = null
        if (cursor.moveToFirst()) {
            monto = cursor.getDouble(cursor.getColumnIndexOrThrow("cuotaMensual"))
        }

        cursor.close()
        return monto
    }

    // Funcion buscar cuando vence la cuota
    fun getVenceCuota(dni: Int): String? {
        val db = readableDatabase
        val query = """
            SELECT venceCuota FROM socio WHERE dni = ?
            UNION
            SELECT venceCuota FROM noSocio WHERE dni = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(dni.toString(), dni.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                return it.getString(0)
            }
        }

        return null
    }

    // Consultar cuota diaria
    fun getDailyFee(dni: Int): Double {
        val db = readableDatabase

        val cursorId = db.rawQuery(
            "SELECT idNoSocio FROM noSocio WHERE dni = ?",
            arrayOf(dni.toString())
        )

        if (!cursorId.moveToFirst()) {
            cursorId.close()
            return 0.0
        }

        val idNoSocio = cursorId.getInt(0)
        cursorId.close()

        val cursorMonto = db.rawQuery(
            """
        SELECT SUM(a.cuotaDiaria)
        FROM actividad a
        INNER JOIN noSocioActividad n ON a.idActividad = n.idActividad
        WHERE n.idNoSocio = ?
        """.trimIndent(),
            arrayOf(idNoSocio.toString())
        )

        val monto = if (cursorMonto.moveToFirst()) cursorMonto.getDouble(0) else 0.0
        cursorMonto.close()
        return monto
    }

    // Eliminar socio
    fun deleteMember(idSocio: Int) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT idSocio, idDireccion FROM socio WHERE idSocio = ?", arrayOf(idSocio.toString()))
        if (cursor.moveToFirst()) {
            val idSocio = cursor.getInt(0)
            val idDireccion = cursor.getInt(1)
            cursor.close()

            db.delete("socio", "idSocio = ?", arrayOf(idSocio.toString()))
            db.delete("direccion", "idDireccion = ?", arrayOf(idDireccion.toString()))
        }
    }

    // Eliminar no socio
    fun deleteNonMember(idNoSocio: Int) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT idNoSocio, idDireccion FROM noSocio WHERE idNoSocio = ?", arrayOf(idNoSocio.toString()))
        if (cursor.moveToFirst()) {
            val idNoSocio = cursor.getInt(0)
            val idDireccion = cursor.getInt(1)
            cursor.close()

            db.delete("noSocio", "idNoSocio = ?", arrayOf(idNoSocio.toString()))
            db.delete("direccion", "idDireccion = ?", arrayOf(idDireccion.toString()))
        }
    }

    // List de actividades disponibles para el socio
    fun getActivitiesNonSuscribeS(idSocio: Int): List<Actividad> {
        val actividades = mutableListOf<Actividad>()
        val db = readableDatabase
        val query = """
        SELECT a.idActividad, a.nombre, a.cuotaDiaria
        FROM actividad a
        WHERE a.idActividad NOT IN (
            SELECT idActividad FROM socioActividad WHERE idSocio = ?
        )
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(idSocio.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idActividad = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val cuotaDiaria = cursor.getDouble(2)

                actividades.add(Actividad(idActividad, nombre, cuotaDiaria))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return actividades
    }

    // Inscribir actividad socio
    fun activitiesSuscribeS(idSocio: Int, idActividad: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idSocio", idSocio)
            put("idActividad", idActividad)
        }
        db.insert("socioActividad", null, values)
    }

    // Consultar actividades inscritas socio
    fun getRegisteredAct(idSocio: Int): List<Actividad> {
        val db = readableDatabase
        val query = """
        SELECT a.idActividad, a.nombre, a.cuotaDiaria
        FROM actividad a
        INNER JOIN socioActividad sa ON a.idActividad = sa.idActividad
        WHERE sa.idSocio = ?
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(idSocio.toString()))
        val actividades = mutableListOf<Actividad>()
        if (cursor.moveToFirst()) {
            do {
                val idActividad = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val cuotaDiaria = cursor.getDouble(2)

                actividades.add(Actividad(idActividad, nombre, cuotaDiaria))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return actividades
    }

    // Desinscribir actividad socio
    fun unsuscribeAct(idSocio: Int, idActividad: Int) {
        val db = writableDatabase
        db.delete(
            "socioActividad",
            "idSocio = ? AND idActividad = ?",
            arrayOf(idSocio.toString(), idActividad.toString())
        )
        db.close()
    }

    // List de actividades disponibles para el no socio
    fun getActivitiesNonSuscribeNS(idNoSocio: Int): List<Actividad> {
        val actividades = mutableListOf<Actividad>()
        val db = readableDatabase
        val query = """
        SELECT a.idActividad, a.nombre, a.cuotaDiaria
        FROM actividad a
        WHERE a.idActividad NOT IN (
            SELECT idActividad FROM noSocioActividad WHERE idNoSocio = ?
        )
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(idNoSocio.toString()))
        if (cursor.moveToFirst()) {
            do {
                val idActividad = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val cuotaDiaria = cursor.getDouble(2)

                actividades.add(Actividad(idActividad, nombre, cuotaDiaria))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return actividades
    }

    // Inscribir actividad no socio
    fun activitiesSuscribeN(idNoSocio: Int, idActividad: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idNoSocio", idNoSocio)
            put("idActividad", idActividad)
        }
        db.insert("noSocioActividad", null, values)
    }

    // Consultar actividades inscritas no socio
    fun getRegisteredActNS(idNoSocio: Int): List<Actividad> {
        val db = readableDatabase
        val query = """
        SELECT a.idActividad, a.nombre, a.cuotaDiaria
        FROM actividad a
        INNER JOIN noSocioActividad nsa ON a.idActividad = nsa.idActividad
        WHERE nsa.idNoSocio = ?
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(idNoSocio.toString()))
        val actividades = mutableListOf<Actividad>()
        if (cursor.moveToFirst()) {
            do {
                val idActividad = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val cuotaDiaria = cursor.getDouble(2)

                actividades.add(Actividad(idActividad, nombre, cuotaDiaria))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return actividades
    }

    // Desinscribir actividad no socio
    fun unsuscribeActNS(idNoSocio: Int, idActividad: Int) {
        val db = writableDatabase
        db.delete(
            "noSocioActividad",
            "idNoSocio = ? AND idActividad = ?",
            arrayOf(idNoSocio.toString(), idActividad.toString())
        )
        db.close()
    }

    // Registrar Usuario y contrasena
    fun registerUser (userName: String, password: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userName", userName)
            put("password", password)
        }
        db.insert("user", null, values)
    }
}