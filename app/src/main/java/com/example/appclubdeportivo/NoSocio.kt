package com.example.appclubdeportivo

class NoSocio (
    dni: Int,
    name: String,
    lastName: String,
    phoneNum: Int,
    nacionality: String,
    override val address: Direccion
) : Persona(dni, name, lastName, phoneNum, nacionality, address) {
}