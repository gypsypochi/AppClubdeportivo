package com.example.appclubdeportivo

class Persona (
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
