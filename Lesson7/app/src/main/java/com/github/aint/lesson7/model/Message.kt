package com.github.aint.lesson7.model

import java.io.Serializable

data class Message(
        var imageId: Int,
        val title: String,
        val body: String) : Serializable