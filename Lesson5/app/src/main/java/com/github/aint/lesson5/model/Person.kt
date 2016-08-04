package com.github.aint.lesson5.model

import java.io.Serializable

data class Person(
        val imageId: Int,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val sex: String,
        val salary: Double,
        val location: String,
        val occupation: String) : Serializable