package com.example.appclubdeportivo

abstract class Persona (
    val dni: Int,
    val name: String,
    val lastName: String,
    val phoneNum: Int,
    val nacionality: String,
    open val address: Direccion
)

data class Direccion (
    val calle: String,
    val altura: Int,
    val barrio: String,
    val localidad: String
)

data class Actividad (
    val id: Int,
    val nombre: String,
    val cuotaDiaria: Double
) {
    override fun toString(): String {
        return "ID: $id - $nombre - Cuota Diaria: $cuotaDiaria"
    }
}