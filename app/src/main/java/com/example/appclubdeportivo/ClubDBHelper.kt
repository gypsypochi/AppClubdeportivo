package com.example.appclubdeportivo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClubDBHelper (context: Context): SQLiteOpenHelper(context, "ClubDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Direccion (
            idDireccion INTENGER PRIMARY KEY AUTOINCREMENT,
            calle TEXT,
            altura INTENGER,
            barrio TEXT,
            localidad TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Socio (
            idSocio INTENGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            apellido TEXT,
            dni INTENGER UNIQUE,
            nacionalidad TEXT,
            telefono INTENGER,
            idDireccion INTENGER,
            venceCuota TEXT,
            FOREIGN KEY (idDireccion) REFERENCES Direccion(idDireccion)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}