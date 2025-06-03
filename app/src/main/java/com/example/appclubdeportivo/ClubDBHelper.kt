package com.example.appclubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClubDBHelper (context: Context): SQLiteOpenHelper(context, "ClubDB", null, 3) {

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
            FOREIGN KEY (idDireccion) REFERENCES direccion(idDireccion)
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
            FOREIGN KEY (idDireccion) REFERENCES direccion(idDireccion)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE actividad (
            idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            arancelMensual DOUBLE,
            arancelDiario DOUBLE,
            maxInscritos INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE socioActividad (
            idSocioActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            idSocio INTEGER,
            idActividad INTEGER,
            FOREIGN KEY (idSocio) REFERENCES socio(idSocio),
            FOREIGN KEY (idActividad) REFERENCES actividad(idActividad)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE noSocioActividad (
            idNoSocioActividad INTEGER PRIMARY KEY AUTOINCREMENT,
            idNoSocio INTEGER,
            idActividad INTEGER,
            FOREIGN KEY (idNoSocio) REFERENCES noSocio(idNoSocio),
            FOREIGN KEY (idActividad) REFERENCES actividad(idActividad)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE user (
            idUser INTEGER PRIMARY KEY AUTOINCREMENT,
            userName TEXT,
            password TEXT
            )
        """.trimIndent())

        db.execSQL("INSERT INTO user (userName, password) VALUES ('Veronica', '12345')")
        db.execSQL("INSERT INTO user (userName, password) VALUES ('Micaela', '12123')")
        db.execSQL("INSERT INTO user (userName, password) VALUES ('Pedro', '12321')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS noSocioActividad")
        db.execSQL("DROP TABLE IF EXISTS socioActividad")
        db.execSQL("DROP TABLE IF EXISTS actividad")
        db.execSQL("DROP TABLE IF EXISTS noSocio")
        db.execSQL("DROP TABLE IF EXISTS socio")
        db.execSQL("DROP TABLE IF EXISTS direccion")

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
        }
        val result = db.insert(tableName, null, values)
        return  result != -1L
    }
}