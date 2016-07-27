package com.github.aint.lesson2.model

import java.io.Serializable
import java.util.*

data class Person(
        var id: Int,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val sex: String,
        val salary: Double,
        val location: String,
        val occupation: String) : Serializable {

    constructor(firstName: String, lastName: String, age: Int, sex: String, salary: Double, location: String, occupation: String)
        : this(UUID.randomUUID().mostSignificantBits.toInt(), firstName, lastName, age, sex, salary, location, occupation)

}